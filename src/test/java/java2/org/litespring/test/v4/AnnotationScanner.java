package java2.org.litespring.test.v4;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import org.junit.Assert;

public class AnnotationScanner {
    private DefaultBeanFactory factory;

    public AnnotationScanner(DefaultBeanFactory factory) {
        this.factory = factory;
    }

    public void scanTest(String annotation) {
//        {
//            BeanDefinition bd = factory.getBeanDefinition("petStore");
//            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
//            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
//            AnnotationMetadata amd = sbd.getMetadata();
//
//            Assert.assertTrue(amd.hasAnnotation(annotation));
//            AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
//            Assert.assertEquals("petStore", attributes.get("value"));
//        }
//        {
//            BeanDefinition bd = factory.getBeanDefinition("accountDao");
//            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
//            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
//            AnnotationMetadata amd = sbd.getMetadata();
//            Assert.assertTrue(amd.hasAnnotation(annotation));
//        }
//        {
//            BeanDefinition bd = factory.getBeanDefinition("itemDao");
//            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
//            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
//            AnnotationMetadata amd = sbd.getMetadata();
//            Assert.assertTrue(amd.hasAnnotation(annotation));
//        }
    }
}
