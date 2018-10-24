package java2.org.litespring.beans;

import java2.org.litespring.beans.factory.config.ConstructorArgumentValues;

import java.util.List;

public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_DEFAULT = "";

    String getScope();

    void setScope(String scope);

    boolean isSingleton();

    boolean isPrototype();

    String getBeanClassName();

    void setBeanClassName(String beanClassName);

    List<PropertyValue> getPropertyValues();

    ConstructorArgumentValues getConstructorArgumentValues();

    boolean hasConstructorArgumentValues();

    Class<?> getBeanClass();

    String getID();

    void setID(String id);
}
