package java2.org.litespring.beans.factory.xml;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.PropertyValue;
import java2.org.litespring.beans.factory.BeanDefinitionStoreException;
import java2.org.litespring.beans.factory.config.ConstructorArgumentValues;
import java2.org.litespring.beans.factory.config.RuntimeBeanReference;
import java2.org.litespring.beans.factory.config.TypedStringValue;
import java2.org.litespring.beans.factory.support.BeanDefinitionRegistry;
import java2.org.litespring.beans.factory.support.GenericBeanDefinition;
import java2.org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";

    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";

    public static final String BASE_PACKAGE_ELEMENT = "base-package";

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

                if (!isDefaultNamespace(element)) {
                    parseComponentElement(element);
                } else {
                    parseDefaultElement(element);
                }
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

    private void parseComponentElement(Element element) {
        boolean hasBasePackage = hasAttribute(element, BASE_PACKAGE_ELEMENT);
        if (!hasBasePackage) {
            throw new IllegalArgumentException("tag component-scan must have base-package attribute");
        }

        String packageToScan = element.attributeValue(BASE_PACKAGE_ELEMENT);
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(this.registry);
        classPathBeanDefinitionScanner.doScan(packageToScan);
    }

    private void parseDefaultElement(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);

        BeanDefinition beanDefinition = parseBeanDefinitions(element);
        if (element.attributeValue(SCOPE_ATTRIBUTE) != null) {
            beanDefinition.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
        }
        this.registry.registerBeanDefinition(id, beanDefinition);
    }

    private BeanDefinition parseBeanDefinitions(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);
        String beanClassName = element.attributeValue(CLASS_ATTRIBUTE);

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition(id, beanClassName);

        parsePropertyElement(element, beanDefinition);
        parseConstructorArgElements(element, beanDefinition);
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

    private void parseConstructorArgElements(Element element, BeanDefinition bd) {
        Iterator iterator = element.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iterator.hasNext()) {
            Element constructorArgElement = (Element) iterator.next();
            boolean hasRef = hasAttribute(constructorArgElement, REF_ATTRIBUTE);
            boolean hasValue = hasAttribute(constructorArgElement, VALUE_ATTRIBUTE);

            if (!hasRef && !hasValue) {
                throw new IllegalArgumentException("constructor-arg must have ref or value");
            }

            Object value = parsePropertyValue(constructorArgElement, bd, null);
            ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
            bd.getConstructorArgumentValues().addArgumentValue(valueHolder);
        }
    }

    private boolean hasAttribute(Element element, String name) {
        return element.attributeValue(name) != null;
    }

    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isDefaultNamespace(Element element) {
        return isDefaultNamespace(getNamespaceURI(element));
    }

    public String getNamespaceURI(Element element) {
        return element.getNamespaceURI();
    }
}
