package java2.org.litespring.beans.factory;

import java2.org.litespring.beans.BeansException;

public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String msg) {
        super(msg);
    }

    public BeanDefinitionStoreException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
