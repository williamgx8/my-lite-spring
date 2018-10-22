package java2.org.litespring.beans.factory;

import java2.org.litespring.beans.BeansException;

public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
