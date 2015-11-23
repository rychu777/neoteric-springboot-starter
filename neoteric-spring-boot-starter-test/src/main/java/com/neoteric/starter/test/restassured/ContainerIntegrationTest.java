package com.neoteric.starter.test.restassured;

import org.springframework.boot.test.WebIntegrationTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@WebIntegrationTest(randomPort = true)
public @interface ContainerIntegrationTest {
}
