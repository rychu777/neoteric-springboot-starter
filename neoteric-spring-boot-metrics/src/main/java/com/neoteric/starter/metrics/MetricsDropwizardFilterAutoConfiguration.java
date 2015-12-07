package com.neoteric.starter.metrics;

import com.codahale.metrics.MetricRegistry;
import com.neoteric.starter.metrics.export.ConsoleWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MetricRegistry.class)
//TODO: Add check if using Jersey?
@EnableConfigurationProperties(JerseyProperties.class)
public class MetricsDropwizardFilterAutoConfiguration {

    @Autowired
    JerseyProperties jerseyProperties;

    @Bean
    @ExportMetricWriter
    MetricWriter consoleWriter() {
        return new ConsoleWriter();
    }

    @Bean
    // Try to disable here execution of MetricFilterAutoConfiguration // Issue to SpringBoot
    public DropwizardMetricsFilter metricFilter(MetricRegistry registry) {
        return new DropwizardMetricsFilter(registry, jerseyProperties.getApplicationPath());
    }
}
