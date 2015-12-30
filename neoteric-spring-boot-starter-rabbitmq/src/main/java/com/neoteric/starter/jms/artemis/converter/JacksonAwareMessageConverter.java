package com.neoteric.starter.jms.artemis.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.starter.jms.Constants;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Based on org.springframework.jms.support.converter.MappingJackson2MessageConverter
 * Marshals to TEXT MessageType
 */
public class JacksonAwareMessageConverter implements MessageConverter, BeanClassLoaderAware {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonAwareMessageConverter.class);

    private final ObjectMapper objectMapper;
    private String typeIdPropertyName;
    private Map<String, Class<?>> idClassMappings = new HashMap<String, Class<?>>();
    private Map<Class<?>, String> classIdMappings = new HashMap<Class<?>, String>();
    private ClassLoader beanClassLoader;

    public JacksonAwareMessageConverter(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper must not be null");
        this.objectMapper = objectMapper;
    }

    public void setTypeIdPropertyName(String typeIdPropertyName) {
        this.typeIdPropertyName = typeIdPropertyName;
    }

    public void setTypeIdMappings(Map<String, Class<?>> typeIdMappings) {
        this.idClassMappings = new HashMap<String, Class<?>>();
        for (Map.Entry<String, Class<?>> entry : typeIdMappings.entrySet()) {
            String id = entry.getKey();
            Class<?> clazz = entry.getValue();
            this.idClassMappings.put(id, clazz);
            this.classIdMappings.put(clazz, id);
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        Message message;
        try {
            message = mapToTextMessage(object, session, this.objectMapper);
        } catch (IOException ex) {
            throw new MessageConversionException("Could not map JSON object [" + object + "]", ex);
        }
        setTypeIdOnMessage(object, message);
        setRequestIdOnMessage(message);
        return message;
    }

    private TextMessage mapToTextMessage(Object object, Session session, ObjectMapper objectMapper)
            throws JMSException, IOException {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, object);
        return session.createTextMessage(writer.toString());
    }


    private void setTypeIdOnMessage(Object object, Message message) throws JMSException {
        if (this.typeIdPropertyName == null) {
            return;
        }
        String typeId = this.classIdMappings.get(object.getClass());
        if (typeId == null) {
            typeId = object.getClass().getName();
        }
        message.setStringProperty(this.typeIdPropertyName, typeId);
    }

    private void setRequestIdOnMessage(Message message) throws JMSException {
        message.setStringProperty(Constants.REQUEST_ID, String.valueOf(MDC.get(Constants.REQUEST_ID)));
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        try {
            JavaType targetJavaType = getJavaTypeForMessage(message);
            setRequestIdOnThreadLocal(message);
            return convertToObject(message, targetJavaType);
        } catch (IOException ex) {
            throw new MessageConversionException("Failed to convert JSON message content", ex);
        }
    }

    private void setRequestIdOnThreadLocal(Message message) throws JMSException {
        String messageRequestId = message.getStringProperty(Constants.REQUEST_ID);
        if (StringUtils.isEmpty(messageRequestId)) {
            messageRequestId = UUID.randomUUID().toString();
            LOG.warn("No REQUEST_ID property found on message: {}. Generating new REQUEST_ID: {}", message, messageRequestId);
        }

        String mdcRequestId = Constants.REQUEST_ID;
        if (!StringUtils.isEmpty(MDC.get(mdcRequestId))) {
            LOG.warn("Something is wrong: REQUEST_ID already exists in MDC. Overwriting {} with {}", mdcRequestId, messageRequestId);
        }
        MDC.put(Constants.REQUEST_ID, messageRequestId);

    }

    protected JavaType getJavaTypeForMessage(Message message) throws JMSException {
        String typeId = message.getStringProperty(this.typeIdPropertyName);
        if (typeId == null) {
            throw new MessageConversionException("Could not find type id property [" + this.typeIdPropertyName + "]");
        }
        Class<?> mappedClass = this.idClassMappings.get(typeId);
        if (mappedClass != null) {
            return this.objectMapper.getTypeFactory().constructType(mappedClass);
        }
        try {
            Class<?> typeClass = ClassUtils.forName(typeId, this.beanClassLoader);
            return this.objectMapper.getTypeFactory().constructType(typeClass);
        } catch (Throwable ex) {
            throw new MessageConversionException("Failed to resolve type id [" + typeId + "]", ex);
        }
    }

    private Object convertToObject(Message message, JavaType targetJavaType) throws JMSException, IOException {
        if (message instanceof TextMessage) {
            return convertFromTextMessage((TextMessage) message, targetJavaType);
        } else {
            throw new IllegalArgumentException("Unsupported message type [" + message.getClass() +
                    "]. MappingJacksonMessageConverter by default only supports TextMessages and BytesMessages.");
        }
    }

    protected Object convertFromTextMessage(TextMessage message, JavaType targetJavaType)
            throws JMSException, IOException {
        String body = message.getText();
        return this.objectMapper.readValue(body, targetJavaType);
    }

}
