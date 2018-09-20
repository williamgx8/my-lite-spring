package java2.org.litespring.test.v1;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.BeanDefinitionStoreException;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.core.io.ClassPathResource;
import java2.org.litespring.service.v1.PetStoreService;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class BeanFactoryTest {
    DefaultBeanFactory factory = null;
    XmlBeanDefinitionReader reader = null;

    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);

    }

    @Test
    public void testGetBean() {

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertTrue(bd.isSingleton());

        assertFalse(bd.isPrototype());

        assertEquals(BeanDefinition.SCOPE_DEFAULT, bd.getScope());

        assertEquals("PetStoreService", bd.getBeanClassName());

        PetStoreService petStore = (PetStoreService) factory.getBean("petStore");

        assertNotNull(petStore);

        PetStoreService petStore1 = (PetStoreService) factory.getBean("petStore");

        assertTrue(petStore.equals(petStore1));
    }

    @Test
    public void testInvalidBean() {

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
        try {
            factory.getBean("invalidBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expect BeanCreationException ");
    }

    @Test
    public void testInvalidXML() {

        try {
            reader.loadBeanDefinitions(new ClassPathResource("xxxx.xml"));
        } catch (BeanDefinitionStoreException e) {
            return;
        }
        Assert.fail("expect BeanDefinitionStoreException ");
    }

}
