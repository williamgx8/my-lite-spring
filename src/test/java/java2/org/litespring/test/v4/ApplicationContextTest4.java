package java2.org.litespring.test.v4;

import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.context.support.ClassPathXmlApplicationContext;
import java2.org.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest4 {
    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

    }
}
