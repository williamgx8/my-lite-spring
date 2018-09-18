package java2.org.litespring.context.support;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private List<String> configLocations;

    public ClassPathXmlApplicationContext(String configLocation) {
        super(configLocation);

        if (configLocation != null) {
            configLocations = new ArrayList<>();
            configLocations.add(configLocation);
        }
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return null;
    }
}