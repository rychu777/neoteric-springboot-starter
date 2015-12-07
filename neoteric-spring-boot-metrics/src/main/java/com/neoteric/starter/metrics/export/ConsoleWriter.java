package com.neoteric.starter.metrics.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

public class ConsoleWriter implements MetricWriter {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleWriter.class);

    @Override
    public void increment(Delta<?> delta) {

        LOG.warn("Increment: {}", delta);
    }

    @Override
    public void reset(String metricName) {

        LOG.warn("reset: {}", metricName);
    }

    @Override
    public void set(Metric<?> value) {
        LOG.warn("set: {}", value);

    }
}
