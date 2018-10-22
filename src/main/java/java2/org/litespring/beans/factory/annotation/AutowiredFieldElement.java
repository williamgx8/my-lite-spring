package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.factory.BeanFactory;

import java.lang.reflect.Field;

public class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

    private final Field field;
    private final boolean required;
    private final BeanFactory beanFactory;

    public AutowiredFieldElement(Field field, boolean required, BeanFactory beanFactory) {
        this.field = field;
        this.required = required;
        this.beanFactory = beanFactory;
    }

    public Field getField() {
        return null;
    }
}
