package java2.org.litespring.test.v4;

import java2.org.litespring.beans.factory.annotation.AutowiredFieldElement;
import java2.org.litespring.beans.factory.annotation.InjectionMetadata;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.core.io.ClassPathResource;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.dao.v4.AccountDao;
import java2.org.litespring.dao.v4.ItemDao;
import java2.org.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class InjectionMetadataTest {
    @Test
    public void testInjection() throws Exception {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Class<?> clz = PetStoreService.class;
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<>();

        {
            Field f = PetStoreService.class.getDeclaredField("accountDao");
            InjectionMetadata.InjectedElement injectionElem = new AutowiredFieldElement(f, true, factory);
            elements.add(injectionElem);
        }
        {
            Field f = PetStoreService.class.getDeclaredField("itemDao");
            InjectionMetadata.InjectedElement injectionElem = new AutowiredFieldElement(f, true, factory);
            elements.add(injectionElem);
        }

        InjectionMetadata metadata = new InjectionMetadata(clz, elements);

        PetStoreService petStore = new PetStoreService();

        metadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);

    }

}
