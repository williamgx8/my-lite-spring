package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.factory.BeanFactory;

public class BeanDefinitionValueResolver {
    private BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object originValue) {

        return null;
    }
}
