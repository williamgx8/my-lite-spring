package java2.org.litespring.test.v4;

import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.core.io.ClassPathResource;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.stereotype.Component;
import org.junit.Test;

public class XmlBeanDefinitionReaderTest {
    @Test
    public void testParseScanedBean() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);
        String annotation = Component.class.getName();

        AnnotationScanner annotationScanner = new AnnotationScanner(factory);
        annotationScanner.scanTest(annotation);
    }
}
