package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import java2.org.litespring.beans.factory.config.DependencyDescriptor;

import java.lang.reflect.Field;

public class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

    private final boolean required;

    public AutowiredFieldElement(Field field, boolean required, AutowireCapableBeanFactory beanFactory) {
        super(field, beanFactory);
        this.required = required;
    }

    public Field getField() {
        return (Field) member;
    }

    @Override
    public void inject(Object target) throws IllegalAccessException {
        Field field = getField();
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(field, this.required);
        Object injectedValue = factory.resolveDependency(dependencyDescriptor);

        if (injectedValue != null) {
            field.setAccessible(true);
            field.set(target, injectedValue);
        }
    }
}
