package java2.org.litespring.context.support;

import java2.org.litespring.core.io.ClassPathResource;
import java2.org.litespring.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private List<String> configLocations;

    public ClassPathXmlApplicationContext(String configLocation) {
        super(configLocation);

        if (configLocation != null) {
            configLocations = new ArrayList<>();
            configLocations.add(configLocation);
        }
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new ClassPathResource(path, this.getBeanClassLoader());
    }

}