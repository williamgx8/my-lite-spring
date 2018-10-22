package java2.org.litespring.context.support;

import java2.org.litespring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.util.ClassUtils;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory beanFactory;
    private ClassLoader classLoader;

    public AbstractApplicationContext(String configFile) {
        beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = getResourceByPath(configFile);
        beanDefinitionReader.loadBeanDefinitions(resource);
        beanFactory.setBeanClassLoader(this.getBeanClassLoader());
        registerBeanPostProcessor();
    }

    protected abstract Resource getResourceByPath(String path);

    public ClassLoader getBeanClassLoader() {
        return classLoader == null ? ClassUtils.getDefaultClassLoader() : this.classLoader;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Object getBean(String id) {
        return beanFactory.getBean(id);
    }

    private void registerBeanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
        this.beanFactory.addBeanPostProcessor(autowiredAnnotationBeanPostProcessor);
    }

}
