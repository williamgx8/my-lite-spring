package java2.org.litespring.test.v3;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.factory.config.ConstructorArgumentValues;
import java2.org.litespring.beans.factory.config.RuntimeBeanReference;
import java2.org.litespring.beans.factory.config.TypedStringValue;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.core.io.ClassPathResource;
import java2.org.litespring.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanDefinitionTestV3 {
    @Test
    public void testConstructorArgument() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition bd = factory.getBeanDefinition("petStore");
        Assert.assertEquals("java2.org.litespring.service.v3.PetStoreService", bd.getBeanClassName());

        ConstructorArgumentValues args = bd.getConstructorArgumentValues();
        List<ConstructorArgumentValues.ValueHolder> valueHolders = args.getArgumentValues();

        Assert.assertEquals(3, valueHolders.size());

        RuntimeBeanReference ref1 = (RuntimeBeanReference) valueHolders.get(0).getValue();
        Assert.assertEquals("accountDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference) valueHolders.get(1).getValue();
        Assert.assertEquals("itemDao", ref2.getBeanName());

        TypedStringValue strValue = (TypedStringValue) valueHolders.get(2).getValue();
        Assert.assertEquals("1", strValue.getValue());
    }
}
