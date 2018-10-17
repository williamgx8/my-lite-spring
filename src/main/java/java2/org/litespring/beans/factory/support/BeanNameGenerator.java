package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;

public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition bd, BeanDefinitionRegistry beanDefinitionRegistry);
}
