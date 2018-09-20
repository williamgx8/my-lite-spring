package java2.org.litespring.beans;

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

    List<PropertyValue> getPropertyValues();

}
