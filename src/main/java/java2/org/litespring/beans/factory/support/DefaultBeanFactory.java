package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.PropertyValue;
import java2.org.litespring.beans.SimpleTypeConverter;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.config.ConfigurableBeanFactory;
import java2.org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
        Object instance = instantiateBean(beanDefinition);
        populateBean(beanDefinition, instance);
        return instance;
    }

    private Object instantiateBean(BeanDefinition bd) {
        ClassLoader cl = getBeanClassLoader();
        String beanClassName = bd.getBeanClassName();
        try {
            if (bd.hasConstructorArgumentValues()) {
                ConstructorResolver constructorResolver = new ConstructorResolver(this);
                Object instance = constructorResolver.autowireConstructor(bd);
                return instance;
            }

            Class<?> targetClass = cl.loadClass(beanClassName);
            Object instance = targetClass.getDeclaredConstructor().newInstance();
            return instance;
        } catch (Exception e) {
            throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
        }
    }

    private void populateBean(BeanDefinition bd, Object bean) {
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (propertyValues.size() == 0) {
            return;
        }

        BeanDefinitionValueResolver bdvr = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyValue pv : propertyValues) {
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = bdvr.resolveValueIfNecessary(originalValue);

                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (!pd.getName().equals(propertyName)) {
                        continue;
                    }

                    Object convertedValue = simpleTypeConverter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                    pd.getWriteMethod().invoke(bean, convertedValue);
                }
            }
        } catch (IntrospectionException e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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
