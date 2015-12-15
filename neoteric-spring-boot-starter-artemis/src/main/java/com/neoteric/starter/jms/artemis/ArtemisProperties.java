package com.neoteric.starter.jms.artemis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.artemis")
public class ArtemisProperties extends org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties {

    private String username;
    private String password;

    private BackOffMode backOffMode = BackOffMode.FIXED;

    /**
     * the maximum number of attempts
     */
    private int maxRetries = 5;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public BackOffMode getBackOffMode() {
        return backOffMode;
    }

    public void setBackOffMode(BackOffMode backOffMode) {
        this.backOffMode = backOffMode;
    }

    public FixedBackOff getFixedBackOff() {
        return fixedBackOff;
    }

    public ExponentialBackOff getExponentialBackOff() {
        return exponentialBackOff;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public enum BackOffMode {
        FIXED, EXPONENTIAL;
    }

    public FixedBackOff fixedBackOff = new FixedBackOff();
    public ExponentialBackOff exponentialBackOff = new ExponentialBackOff();

    public static class FixedBackOff {

        /**
         * the interval between two attempts
         */
        private long interval = 30000;

        public long getInterval() {
            return interval;
        }

        public void setInterval(long interval) {
            this.interval = interval;
        }
    }

    public static class ExponentialBackOff {

        /**
         * the initial interval in milliseconds
         */
        private long initialInterval = 10000; // 10 sec

        /**
         * the multiplier (should be greater than or equal to 1)
         */
        private double multiplier = 5;

        /**
         * Return the maximum back off time.
         */
        private long maximumInterval = 900000; // 15 min

        public long getInitialInterval() {
            return initialInterval;
        }

        public void setInitialInterval(long initialInterval) {
            this.initialInterval = initialInterval;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

        public long getMaximumInterval() {
            return maximumInterval;
        }

        public void setMaximumInterval(long maximumInterval) {
            this.maximumInterval = maximumInterval;
        }
    }
}
