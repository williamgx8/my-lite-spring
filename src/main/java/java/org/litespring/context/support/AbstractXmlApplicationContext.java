package java.org.litespring.context.support;

import java.org.litespring.beans.BeanDefinition;
import java.org.litespring.context.ApplicationContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractXmlApplicationContext implements ApplicationContext{
    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    protected AbstractXmlApplicationContext(String filename){

    }
}
