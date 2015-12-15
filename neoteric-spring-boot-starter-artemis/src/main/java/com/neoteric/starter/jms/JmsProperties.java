package com.neoteric.starter.jms;

public class JmsProperties extends org.springframework.boot.autoconfigure.jms.JmsProperties {

    private final Listener listener = new Listener();

    @Override
    public Listener getListener() {
        return this.listener;
    }

    public static class Listener extends org.springframework.boot.autoconfigure.jms.JmsProperties.Listener {
        private String clientId;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
    }
}
