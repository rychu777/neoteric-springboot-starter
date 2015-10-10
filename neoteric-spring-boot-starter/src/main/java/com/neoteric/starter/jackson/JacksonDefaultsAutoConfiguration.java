package com.neoteric.starter.jackson;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jackson-defaults.properties")
public class JacksonDefaultsAutoConfiguration {
}
