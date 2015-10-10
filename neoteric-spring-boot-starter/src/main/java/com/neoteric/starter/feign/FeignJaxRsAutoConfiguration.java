package com.neoteric.starter.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.starter.tracing.RequestIdAppendInterceptor;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Feign.class)
@EnableConfigurationProperties(FeignProperties.class)
public class FeignJaxRsAutoConfiguration {

    @Autowired
    FeignProperties feignProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public Contract feignContract() {
        return new JAXRSContract();
    }

    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public RequestIdAppendInterceptor idAppendInterceptor() {
        return new RequestIdAppendInterceptor();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.valueOf(feignProperties.getLoggerLevel());
    }
}
