package java2.org.litespring.test.v3;

import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.context.support.ClassPathXmlApplicationContext;
import java2.org.litespring.service.v3.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTestV3 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());
        Assert.assertEquals(1, petStore.getVersion());

    }

}
