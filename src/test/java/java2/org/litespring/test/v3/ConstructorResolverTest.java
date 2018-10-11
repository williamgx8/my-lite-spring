package java2.org.litespring.test.v3;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.factory.support.ConstructorResolver;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import java2.org.litespring.core.io.ClassPathResource;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.service.v3.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ConstructorResolverTest {
    @Test
    public void testAutowireConstructor() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        ConstructorResolver resolver = new ConstructorResolver(factory);

        PetStoreService petStore = (PetStoreService) resolver.autowireConstructor(bd);

        // 验证参数version 正确地通过此构造函数做了初始化
        // PetStoreService(AccountDao accountDao, ItemDao itemDao,int version)
        Assert.assertEquals(1, petStore.getVersion());

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

    }
}
