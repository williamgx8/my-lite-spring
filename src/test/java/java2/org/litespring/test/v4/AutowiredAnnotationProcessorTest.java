package java2.org.litespring.test.v4;

import java2.org.litespring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import java2.org.litespring.beans.factory.annotation.AutowiredFieldElement;
import java2.org.litespring.beans.factory.annotation.InjectionMetadata;
import java2.org.litespring.beans.factory.config.DependencyDescriptor;
import java2.org.litespring.beans.factory.support.DefaultBeanFactory;
import java2.org.litespring.dao.v4.AccountDao;
import java2.org.litespring.dao.v4.ItemDao;
import java2.org.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class AutowiredAnnotationProcessorTest {
    AccountDao accountDao = new AccountDao();
    ItemDao itemDao = new ItemDao();
    DefaultBeanFactory beanFactory = new DefaultBeanFactory() {
        @Override
        public Object resolveDependency(DependencyDescriptor descriptor) {
            if (descriptor.getDependencyType().equals(AccountDao.class)) {
                return accountDao;
            }
            if (descriptor.getDependencyType().equals(ItemDao.class)) {
                return itemDao;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };

    @Test
    public void testGetInjectionMetadata() throws IllegalAccessException {

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);
        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionMetadata.InjectedElement> elements = (List<InjectionMetadata.InjectedElement>) injectionMetadata.getInjectedElements();
        Assert.assertEquals(2, elements.size());

        assertFieldExists(elements, "accountDao");
        assertFieldExists(elements, "itemDao");

        PetStoreService petStore = new PetStoreService();

        injectionMetadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);
    }

    private void assertFieldExists(List<InjectionMetadata.InjectedElement> elements, String fieldName) {
        for (InjectionMetadata.InjectedElement ele : elements) {
            AutowiredFieldElement fieldEle = (AutowiredFieldElement) ele;
            Field f = fieldEle.getField();
            if (f.getName().equals(fieldName)) {
                return;
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }
}
