package java.org.litespring.beans;

public interface BeanDefinition {

     String SCOPE_SINGLETON = "singleton";
     String SCOPE_PROTOTYPE = "prototype";


     String getScope();

    void setScope(String scope);

    boolean isSingleton();
    boolean isPrototype();

    String getBeanClassName();

}
