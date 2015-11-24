package com.neoteric.starter.test.reinject;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

class ReinjectFactoryBean implements FactoryBean, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final Object object;
    private final Class objectType;

    public ReinjectFactoryBean(Object object, Class objectType) {
        this.object = object;
        this.objectType = objectType;
    }

    @Override
    public Object getObject() throws Exception {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return objectType == null ? object.getClass(): objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
