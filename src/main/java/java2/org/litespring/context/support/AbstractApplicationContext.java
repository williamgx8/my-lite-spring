package java2.org.litespring.context.support;

import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.core.io.Resource;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory beanFactory;

    public AbstractApplicationContext(String configFile) {
        beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = getResourceByPath(configFile);
        beanDefinitionReader.loadBeanDefinitions(resource);
        beanFactory.setBeanClassLoader(this.getBeanClassLoader());
    }

    protected abstract Resource getResourceByPath(String path);

    @Override
    public ClassLoader getBeanClassLoader() {
        return beanFactory.getBeanClassLoader();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        beanFactory.setBeanClassLoader(classLoader);
    }

    @Override
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

}
