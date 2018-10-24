package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.PropertyValue;
import java2.org.litespring.beans.SimpleTypeConverter;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.config.BeanPostProcessor;
import java2.org.litespring.beans.factory.config.ConfigurableBeanFactory;
import java2.org.litespring.beans.factory.config.DependencyDescriptor;
import java2.org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import java2.org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements BeanDefinitionRegistry, ConfigurableBeanFactory {

    /**
     * key保存bean的唯一标示，可能是id属性，也可能是生成的某个唯一串，
     * value保存class属性对应的封装对象BeanDefinition，该对象并不是创建的实例，其中最主要的就是包含了bean的全路径名
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private ClassLoader beanClassLoader;
    private boolean hasInstantiationAwareBeanPostProcessors;

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

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
            /**
             * 处理xml中<constructor></constructor>标签，选取恰当的构造器创建对象实例
             */
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
        /**
         * 解析@Autowired待注入内容，实现DI
         */
        if (this.hasInstantiationAwareBeanPostProcessors) {
            for (BeanPostProcessor beanPostProcessor : this.beanPostProcessors) {
                if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                    ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(bean, bd.getBeanClassName());
                }
            }
        }

        /**
         * 解析xml中
         * <p>
         *     <bean id="xxx" class = "xxxx">
         *         <property name = "xxx" value = "xxx"></property>
         *         <property name = "xxx" ref = "xxx"></property>
         *     </bean>
         * </p>
         * 两种类型
         */
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

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class beanType = descriptor.getDependencyType();
        for (BeanDefinition bd : this.beanDefinitionMap.values()) {

            Class<?> beanClass = bd.getBeanClass();
            /**
             * 只能注入该类的子类
             */
            if (beanType.isAssignableFrom(beanClass)) {
                Object bean = this.getBean(bd.getID());
                return bean;
            }

        }
        return null;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
        this.hasInstantiationAwareBeanPostProcessors = true;
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
}
