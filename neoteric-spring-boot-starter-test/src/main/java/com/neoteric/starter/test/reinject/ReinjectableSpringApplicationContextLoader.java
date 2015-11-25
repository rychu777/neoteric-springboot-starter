package com.neoteric.starter.test.reinject;

import com.neoteric.starter.test.restassured.ContainerIntegrationTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.web.ServletContextApplicationContextInitializer;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringBootMockServletContext;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.test.context.support.AnnotationConfigContextLoaderUtils;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ReinjectableSpringApplicationContextLoader extends AbstractContextLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ReinjectableSpringApplicationContextLoader.class);

    @Override
    public ApplicationContext loadContext(final MergedContextConfiguration config)
            throws Exception {

        assertValidAnnotations(config.getTestClass());
        SpringApplication application = getSpringApplication();
        application.setMainApplicationClass(config.getTestClass());
        application.setSources(getSources(config));
        ConfigurableEnvironment environment = new StandardEnvironment();
        if (!ObjectUtils.isEmpty(config.getActiveProfiles())) {
            setActiveProfiles(environment, config.getActiveProfiles());
        }
        Map<String, Object> properties = getEnvironmentProperties(config);
        addProperties(environment, properties);
        application.setEnvironment(environment);
        List<ApplicationContextInitializer<?>> initializers = getInitializers(config,
                application);
        if (config instanceof WebMergedContextConfiguration) {
            new WebConfigurer().configure(config, application, initializers);
        } else {
            application.setWebEnvironment(false);
        }
        application.setInitializers(initializers);
        addReinjections(config.getTestClass());
        return application.run();
    }

    private void addReinjections(Class<?> testClass) {
        Method[] declaredMethods = testClass.getDeclaredMethods();
        Arrays.stream(declaredMethods)
                .filter(method -> method.getAnnotation(ReinjectBean.class) != null)
                .forEach(method -> {
                    ReinjectBean annotation = method.getAnnotation(ReinjectBean.class);
                    try {
                        Object invoked = method.invoke(null);
                        Reinjector.inject(annotation.value(), invoked);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException("Unable to invoke reinject method", e);
                    }
                });

        Arrays.stream(declaredMethods)
                .filter(method -> method.getAnnotation(Reinject.class) != null)
                .forEach(method -> {
                    try {
                        method.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException("Unable to invoke reinject method", e);
                    }
                });
    }

    private void assertValidAnnotations(Class<?> testClass) {
        boolean hasWebAppConfiguration = AnnotationUtils.findAnnotation(testClass,
                WebAppConfiguration.class) != null;
        boolean hasWebIntegrationTest = AnnotationUtils.findAnnotation(testClass,
                WebIntegrationTest.class) != null;
        if (hasWebAppConfiguration && hasWebIntegrationTest) {
            throw new IllegalStateException("@WebIntegrationTest and "
                    + "@WebAppConfiguration cannot be used together");
        }
    }

    /**
     * Builds new {@link org.springframework.boot.SpringApplication} instance. You can
     * override this method to add custom behavior
     *
     * @return {@link org.springframework.boot.SpringApplication} instance
     */
    protected SpringApplication getSpringApplication() {
        return new SpringApplication();
    }

    private Set<Object> getSources(MergedContextConfiguration mergedConfig) {
        Set<Object> sources = new LinkedHashSet<Object>();
        sources.addAll(Arrays.asList(mergedConfig.getClasses()));
        sources.addAll(Arrays.asList(mergedConfig.getLocations()));
        Assert.state(sources.size() > 0, "No configuration classes "
                + "or locations found in @SpringApplicationConfiguration. "
                + "For default configuration detection to work you need "
                + "Spring 4.0.3 or better (found " + SpringVersion.getVersion() + ").");
        return sources;
    }

    private void setActiveProfiles(ConfigurableEnvironment environment,
                                   String[] profiles) {
        EnvironmentTestUtils.addEnvironment(environment, "spring.profiles.active="
                + StringUtils.arrayToCommaDelimitedString(profiles));
    }

    protected Map<String, Object> getEnvironmentProperties(
            MergedContextConfiguration config) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        // JMX bean names will clash if the same bean is used in multiple contexts
        disableJmx(properties);
        properties.putAll(TestPropertySourceUtils
                .convertInlinedPropertiesToMap(config.getPropertySourceProperties()));
        if (!TestAnnotations.isIntegrationTest(config)) {
            properties.putAll(getDefaultEnvironmentProperties());
        }
        return properties;
    }

    private void disableJmx(Map<String, Object> properties) {
        properties.put("spring.jmx.enabled", "false");
    }

    private Map<String, String> getDefaultEnvironmentProperties() {
        return Collections.singletonMap("server.port", "-1");
    }

    private void addProperties(ConfigurableEnvironment environment,
                               Map<String, Object> properties) {
        // @IntegrationTest properties go before external configuration and after system
        environment.getPropertySources().addAfter(
                StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new MapPropertySource("integrationTest", properties));
    }

    private List<ApplicationContextInitializer<?>> getInitializers(
            MergedContextConfiguration mergedConfig, SpringApplication application) {
        List<ApplicationContextInitializer<?>> initializers = new ArrayList<ApplicationContextInitializer<?>>();
        initializers.add(new PropertySourceLocationsInitializer(
                mergedConfig.getPropertySourceLocations()));
        initializers.addAll(application.getInitializers());
        for (Class<? extends ApplicationContextInitializer<?>> initializerClass : mergedConfig
                .getContextInitializerClasses()) {
            initializers.add(BeanUtils.instantiate(initializerClass));
        }
        return initializers;
    }

    @Override
    public void processContextConfiguration(
            ContextConfigurationAttributes configAttributes) {
        super.processContextConfiguration(configAttributes);
        if (!configAttributes.hasResources()) {
            Class<?>[] defaultConfigClasses = detectDefaultConfigurationClasses(
                    configAttributes.getDeclaringClass());
            configAttributes.setClasses(defaultConfigClasses);
        }
    }

    /**
     * Detect the default configuration classes for the supplied test class. By default
     * simply delegates to
     * {@link AnnotationConfigContextLoaderUtils#detectDefaultConfigurationClasses} .
     *
     * @param declaringClass the test class that declared {@code @ContextConfiguration}
     * @return an array of default configuration classes, potentially empty but never
     * {@code null}
     * @see AnnotationConfigContextLoaderUtils
     */
    protected Class<?>[] detectDefaultConfigurationClasses(Class<?> declaringClass) {
        return AnnotationConfigContextLoaderUtils
                .detectDefaultConfigurationClasses(declaringClass);
    }

    @Override
    public ApplicationContext loadContext(String... locations) throws Exception {
        throw new UnsupportedOperationException("SpringApplicationContextLoader "
                + "does not support the loadContext(String...) method");
    }

    @Override
    protected String[] getResourceSuffixes() {
        return new String[]{"-context.xml", "Context.groovy"};
    }

    @Override
    protected String getResourceSuffix() {
        throw new IllegalStateException();
    }

    /**
     * Inner class to configure {@link WebMergedContextConfiguration}.
     */
    private static class WebConfigurer {

        private static final Class<GenericWebApplicationContext> WEB_CONTEXT_CLASS = GenericWebApplicationContext.class;

        void configure(MergedContextConfiguration configuration,
                       SpringApplication application,
                       List<ApplicationContextInitializer<?>> initializers) {
            if (!TestAnnotations.isIntegrationTest(configuration)) {
                WebMergedContextConfiguration webConfiguration = (WebMergedContextConfiguration) configuration;
                addMockServletContext(initializers, webConfiguration);
                application.setApplicationContextClass(WEB_CONTEXT_CLASS);
            }
        }

        private void addMockServletContext(
                List<ApplicationContextInitializer<?>> initializers,
                WebMergedContextConfiguration webConfiguration) {
            SpringBootMockServletContext servletContext = new SpringBootMockServletContext(
                    webConfiguration.getResourceBasePath());
            initializers.add(0,
                    new ServletContextApplicationContextInitializer(servletContext));
        }

    }

    /**
     * {@link ApplicationContextInitializer} to setup test property source locations.
     */
    private static class PropertySourceLocationsInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final String[] propertySourceLocations;

        PropertySourceLocationsInitializer(String[] propertySourceLocations) {
            this.propertySourceLocations = propertySourceLocations;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addPropertiesFilesToEnvironment(applicationContext,
                    this.propertySourceLocations);
        }

    }

    public static class ReinjectInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            LOG.error("INIIIIIIIIIIT");
            applicationContext.addBeanFactoryPostProcessor(new Reinjector());
        }
    }

    private static class TestAnnotations {

        public static boolean isIntegrationTest(
                MergedContextConfiguration configuration) {
            return (hasAnnotation(configuration, IntegrationTest.class)
                    || hasAnnotation(configuration, ContainerIntegrationTest.class)
                    || hasAnnotation(configuration, WebIntegrationTest.class));
        }

        private static boolean hasAnnotation(MergedContextConfiguration configuration,
                                             Class<? extends Annotation> annotation) {
            return (AnnotationUtils.findAnnotation(configuration.getTestClass(),
                    annotation) != null);
        }

    }
}
