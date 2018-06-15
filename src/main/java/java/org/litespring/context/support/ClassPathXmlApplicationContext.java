package java.org.litespring.context.support;

import java.org.litespring.context.ApplicationContext;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    public ClassPathXmlApplicationContext(String filename) {
        super(filename);
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
