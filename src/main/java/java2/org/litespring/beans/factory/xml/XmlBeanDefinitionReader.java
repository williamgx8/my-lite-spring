package java2.org.litespring.beans.factory.xml;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.PropertyValue;
import java2.org.litespring.beans.factory.BeanDefinitionStoreException;
import java2.org.litespring.beans.factory.config.RuntimeBeanReference;
import java2.org.litespring.beans.factory.config.TypedStringValue;
import java2.org.litespring.beans.factory.support.BeanDefinitionRegistry;
import java2.org.litespring.beans.factory.support.GenericBeanDefinition;
import java2.org.litespring.core.io.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {
    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String NAME_ATTRIBUTE = "name";

    private BeanDefinitionRegistry registry;

    protected final Log logger = LogFactory.getLog(getClass());

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

                BeanDefinition beanDefinition = parseBeanDefinitions(element);
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

    private BeanDefinition parseBeanDefinitions(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);
        String beanClassName = element.attributeValue(CLASS_ATTRIBUTE);

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition(id, beanClassName);

        parsePropertyElement(element, beanDefinition);
        return beanDefinition;
    }

    private void parsePropertyElement(Element element, GenericBeanDefinition beanDefinition) {
        Iterator iterator = element.elementIterator(PROPERTY_ELEMENT);
        while (iterator.hasNext()) {
            Element property = (Element) iterator.next();
            boolean hasName = hasAttribute(property, NAME_ATTRIBUTE);

            if (!hasName) {
                throw new IllegalArgumentException("Tag property must have name attribute");
            }

            String propertyName = property.attributeValue(NAME_ATTRIBUTE);
            Object val = parsePropertyValue(property, beanDefinition, propertyName);
            PropertyValue propertyValue = new PropertyValue(propertyName, val);

            beanDefinition.getPropertyValues().add(propertyValue);
        }
    }

    private Object parsePropertyValue(Element property, BeanDefinition bd, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRef = hasAttribute(property, REF_ATTRIBUTE);
        boolean hasValue = hasAttribute(property, VALUE_ATTRIBUTE);
        if (!hasRef && !hasValue) {
            throw new IllegalArgumentException("Tag property must have ref or value");
        }

        if (hasRef) {
            String refValue = property.attributeValue(REF_ATTRIBUTE);
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(refValue);
            return runtimeBeanReference;
        } else if (hasValue) {
            String value = property.attributeValue(VALUE_ATTRIBUTE);
            TypedStringValue typedStringValue = new TypedStringValue(value);
            return typedStringValue;
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }

    }

    private boolean hasAttribute(Element element, String name) {
        return element.attributeValue(name) != null;
    }
}
