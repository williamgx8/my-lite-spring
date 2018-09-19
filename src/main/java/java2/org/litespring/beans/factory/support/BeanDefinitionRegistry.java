package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName);
}
