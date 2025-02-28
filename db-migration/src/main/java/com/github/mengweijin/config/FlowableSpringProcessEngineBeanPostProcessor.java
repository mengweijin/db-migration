package com.github.mengweijin.config;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.util.ReflectUtil;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author mengweijin
 */
@Configuration
@ConditionalOnClass({SpringProcessEngineConfiguration.class})
public class FlowableSpringProcessEngineBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@Nullable Object bean, @Nullable String beanName) throws BeansException {
        if(bean instanceof SpringProcessEngineConfiguration) {
            Properties databaseTypeMappings = SpringProcessEngineConfiguration.getDefaultDatabaseTypeMappings();
            databaseTypeMappings.setProperty("DM DBMS", AbstractEngineConfiguration.DATABASE_TYPE_ORACLE);
            //databaseTypeMappings.setProperty("GBase 8s Server", "gbase8s");

            Field field = ReflectUtil.getField("databaseTypeMappings", bean);
            ReflectUtil.setField(field, bean, databaseTypeMappings);
        }

        return bean;
    }

}
