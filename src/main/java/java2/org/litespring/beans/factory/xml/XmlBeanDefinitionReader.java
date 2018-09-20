package java2.org.litespring.beans.factory.xml;

import java2.org.litespring.beans.factory.BeanDefinitionStoreException;
import java2.org.litespring.beans.factory.support.BeanDefinitionRegistry;
import java2.org.litespring.beans.factory.support.GenericBeanDefinition;
import java2.org.litespring.core.io.Resource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {
    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void loadBeanDefinitions(Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();

            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element root = document.getRootElement();

            Iterator iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                String id = element.attributeValue(ID_ATTRIBUTE);
                String beanName = element.attributeValue(CLASS_ATTRIBUTE);

                GenericBeanDefinition beanDefinition = new GenericBeanDefinition(id, beanName);
                if (element.attributeValue(SCOPE_ATTRIBUTE) != null) {
                    beanDefinition.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
                }
                this.registry.registerBeanDefinition(id, beanDefinition);
            }

        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
