package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.BeansException;
import java2.org.litespring.beans.PropertyValue;
import java2.org.litespring.beans.factory.config.ConstructorArgumentValues;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {
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
        try {
            return Class.forName(this.beanClassName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("bean class name " + this.beanClassName + " cannot match any Class");
        }
    }

}
