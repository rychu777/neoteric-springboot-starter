package com.neoteric.starter.jersey;

import com.google.common.base.Strings;
import com.neoteric.starter.Constants;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.RegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.RequestContextFilter;

import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

@Configuration
@ConditionalOnClass(name = {
        "org.glassfish.jersey.server.spring.SpringComponentProvider",
        "javax.servlet.ServletRegistration"})
@ConditionalOnBean(type = "org.glassfish.jersey.server.ResourceConfig")
@ConditionalOnWebApplication
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(DispatcherServletAutoConfiguration.class)
@EnableConfigurationProperties(CustomJerseyProperties.class)
public class JerseyAutoConfiguration implements WebApplicationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyAutoConfiguration.class);

    @Autowired
    private CustomJerseyProperties jersey;

    @Autowired
    private ListableBeanFactory context;

    @Autowired
    private ResourceConfig config;

    private String path;

    @PostConstruct
    public void path() {
        this.path = findPath(jersey, AnnotationUtils.findAnnotation(this.config.getClass(),
                ApplicationPath.class));
        LOG.warn("{} Jersey application path: {}", Constants.LOG_PREFIX, path);
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean requestContextFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestContextFilter());
        registration.setOrder(this.jersey.getFilter().getOrder() - 1);
        registration.setName("requestContextFilter");
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean(name = "jerseyFilterRegistration")
    @ConditionalOnProperty(prefix = "spring.jersey", name = "type", havingValue = "filter")
    public FilterRegistrationBean jerseyFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ServletContainer(this.config));
        registration.setUrlPatterns(Arrays.asList(this.path));
        registration.setOrder(this.jersey.getFilter().getOrder());
        registration.addInitParameter(ServletProperties.FILTER_CONTEXT_PATH,
                stripPattern(this.path));
        addInitParameters(registration);
        registration.setName("jerseyFilter");
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }

    private String stripPattern(String path) {
        if (path.endsWith("/*")) {
            path = path.substring(0, path.lastIndexOf("/*"));
        }
        return path;
    }

    @Bean
    @ConditionalOnMissingBean(name = "jerseyServletRegistration")
    @ConditionalOnProperty(prefix = "spring.jersey", name = "type", havingValue = "servlet", matchIfMissing = true)
    public ServletRegistrationBean jerseyServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new ServletContainer(this.config), this.path);
        addInitParameters(registration);
        registration.setName("jerseyServlet");
        return registration;
    }

    private void addInitParameters(RegistrationBean registration) {
        for (Map.Entry<String, String> entry : this.jersey.getInit().entrySet()) {
            registration.addInitParameter(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // We need to switch *off* the Jersey WebApplicationInitializer because it
        // will try and register a ContextLoaderListener which we don't need
        servletContext.setInitParameter("contextConfigLocation", "<NONE>");
    }

    private static String findPath(CustomJerseyProperties urlJersey, ApplicationPath annotation) {
        String path;
        if (!Strings.isNullOrEmpty(urlJersey.getBaseURI())) {
            path = urlJersey.getBaseURI();
        } else if (annotation == null) {
            // Jersey doesn't like to be the default servlet, so map to /* as a fallback
            return "/*";
        } else {
            path = annotation.value();
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path.equals("/") ? "/*" : path + "/*";
    }
}
