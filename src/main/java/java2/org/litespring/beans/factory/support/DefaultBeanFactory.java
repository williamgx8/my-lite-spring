package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.config.ConfigurableBeanFactory;
import java2.org.litespring.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements BeanDefinitionRegistry, ConfigurableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private ClassLoader beanClassLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override
    public Object getBean(String id) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(id);
        if (beanDefinition == null) {
            throw new BeanCreationException("cannot find BeanDefinition by id = " + id);
        }
        if (beanDefinition.isSingleton()) {
            Object singleton = getSingleton(id);
            if (singleton == null) {
                singleton = createBean(beanDefinition);
                this.registerSingleton(id, singleton);
            }
            return singleton;
        }
        return createBean(beanDefinition);
    }

    private Object createBean(BeanDefinition beanDefinition) {
        ClassLoader cl = getBeanClassLoader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> targetClass = cl.loadClass(beanClassName);
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean class " + beanClassName + " error", e);
        }
    }

    @Override
    public void registerBeanDefinition(String id, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(id, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String id) {
        return this.beanDefinitionMap.get(id);
    }
}
