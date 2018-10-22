package java2.org.litespring.beans.factory.config;

import java2.org.litespring.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    Object resolveDependency(DependencyDescriptor dependencyDescriptor);
}
