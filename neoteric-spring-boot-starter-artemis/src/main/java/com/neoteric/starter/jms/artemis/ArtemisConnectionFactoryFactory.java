package com.neoteric.starter.jms.artemis;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

class ArtemisConnectionFactoryFactory {

    private final ArtemisProperties properties;

    ArtemisConnectionFactoryFactory(ArtemisProperties properties) {
        Assert.notNull(properties, "Properties must not be null");
        this.properties = properties;
    }

    public <T extends ActiveMQConnectionFactory> T createConnectionFactory(
            Class<T> factoryClass) {
        try {
            return doCreateConnectionFactory(factoryClass);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Unable to create " + "ActiveMQConnectionFactory", ex);
        }
    }

    private <T extends ActiveMQConnectionFactory> T doCreateConnectionFactory(
            Class<T> factoryClass) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(TransportConstants.HOST_PROP_NAME, this.properties.getHost());
        params.put(TransportConstants.PORT_PROP_NAME, this.properties.getPort());
        TransportConfiguration transportConfiguration = new TransportConfiguration(
                NettyConnectorFactory.class.getName(), params);
        Constructor<T> constructor = factoryClass.getConstructor(boolean.class,
                TransportConfiguration[].class);
        return constructor.newInstance(false,
                new TransportConfiguration[]{transportConfiguration});
    }
}
