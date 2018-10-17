package java2.org.litespring.test.v4;

import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import java2.org.litespring.stereotype.Component;
import org.junit.Test;

public class ClassPathBeanDefinitionScannerTest {
    @Test
    public void testParseScanedBean() {

        DefaultBeanFactory factory = new DefaultBeanFactory();

        String basePackages = "java2.org.litespring.service.v4,java2.org.litespring.dao.v4";

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.doScan(basePackages);

        String annotation = Component.class.getName();

        AnnotationScanner annotationScanner = new AnnotationScanner(factory);
        annotationScanner.scanTest(annotation);
    }
}
