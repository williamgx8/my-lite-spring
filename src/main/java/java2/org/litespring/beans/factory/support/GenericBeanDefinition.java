package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.BeansException;
import java2.org.litespring.beans.PropertyValue;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.config.ConstructorArgumentValues;
import java2.org.litespring.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {
    /**
     * BeanDefinition的唯一标识，在Spring中不存在这个id属性，而是通过beanClassName得到的一个唯一标示，
     * 没有用单独的变量保存
     */
    private String id;
    private String beanClassName;
    private Class<?> beanClass;
    private boolean isSingleton = true;
    private boolean isPrototype = false;
    private String scope = SCOPE_DEFAULT;
    private final List<PropertyValue> propertyValues = new ArrayList<>();
    private ConstructorArgumentValues constructorArgumentValues;

    public GenericBeanDefinition() {
        super();
    }

    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
        this.isSingleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.isPrototype = SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public boolean isSingleton() {
        return isSingleton;
    }

    @Override
    public boolean isPrototype() {
        return isPrototype;
    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    @Override
    public ConstructorArgumentValues getConstructorArgumentValues() {
        if (constructorArgumentValues == null) {
            this.constructorArgumentValues = new ConstructorArgumentValues();
        }
        return this.constructorArgumentValues;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return !getConstructorArgumentValues().isEmpty();
    }

    @Override
    public Class<?> getBeanClass() {
        if (this.beanClass == null) {
            try {
                resolveBeanClass(ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException e) {
                throw new BeanCreationException("resolve bean class error", e);
            }
        }
        return this.beanClass;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    private void resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> clazz = classLoader.loadClass(this.beanClassName);
        this.beanClass = clazz;
    }

}
