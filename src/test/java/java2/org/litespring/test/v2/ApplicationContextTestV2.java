package java2.org.litespring.test.v2;

import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.context.support.ClassPathXmlApplicationContext;
import java2.org.litespring.dao.v2.AccountDao;
import java2.org.litespring.dao.v2.ItemDao;
import java2.org.litespring.service.v2.PetStoreService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ApplicationContextTestV2 {
    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");

        assertNotNull(petStore.getAccountDao());
        assertNotNull(petStore.getItemDao());

        assertTrue(petStore.getAccountDao() instanceof AccountDao);
        assertTrue(petStore.getItemDao() instanceof ItemDao);

        assertEquals("liuxin", petStore.getOwner());
        assertEquals(2, petStore.getVersion());

    }
}
