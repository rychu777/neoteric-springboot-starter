package com.neoteric.starter.test;

import com.neoteric.starter.test.reinject.ReinjectableSpringApplicationContextLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@ContextConfiguration(loader = ReinjectableSpringApplicationContextLoader.class)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReinjectableSpringBootTest {

    /**
     * The context configuration classes.
     * @see ContextConfiguration#classes()
     * @return the context configuration classes
     */
    @AliasFor("classes")
    Class<?>[] value() default {};

    /**
     * The context configuration locations.
     * @see ContextConfiguration#locations()
     * @return the context configuration locations
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "locations")
    String[] locations() default {};

    /**
     * The context configuration classes.
     * @see ContextConfiguration#classes()
     * @return the context configuration classes
     */
    @AliasFor("value")
    Class<?>[] classes() default {};

    /**
     * The context configuration initializers.
     * @see ContextConfiguration#initializers()
     * @return the context configuration initializers
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "initializers")
    Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>[] initializers() default {};

    /**
     * Should context locations be inherited.
     * @see ContextConfiguration#inheritLocations()
     * @return {@code true} if context locations should be inherited
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "inheritLocations")
    boolean inheritLocations() default true;

    /**
     * Should initializers be inherited.
     * @see ContextConfiguration#inheritInitializers()
     * @return {@code true} if context initializers should be inherited
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "inheritInitializers")
    boolean inheritInitializers() default true;

    /**
     * The name of the context hierarchy level.
     * @see ContextConfiguration#name()
     * @return the name of the context hierarchy level
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "name")
    String name() default "";
}
