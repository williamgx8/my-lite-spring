package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.factory.BeanFactory;
import java2.org.litespring.beans.factory.config.RuntimeBeanReference;
import java2.org.litespring.beans.factory.config.TypedStringValue;

/**
 * 对于 PropertyValue中保存的类型到真实类型的转换，比如在PropertyValue中有RuntimeBeanReference、TypedStringValue、ManagedArray、ManageSet等，
 * 除了基本数据类型和字符串仍然返回对应string外都需要靠该类进行解析转换，比如将RuntimeBeanReference转换成对应的bean实例返回
 */
public class BeanDefinitionValueResolver {
    private BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object originValue) {
        if (originValue instanceof RuntimeBeanReference) {
            Object bean = beanFactory.getBean(((RuntimeBeanReference) originValue).getBeanName());
            return bean;
        } else if (originValue instanceof TypedStringValue) {
            return ((TypedStringValue) originValue).getValue();
        } else {
            throw new RuntimeException("the value " + originValue + " has not implemented");
        }
    }
}
