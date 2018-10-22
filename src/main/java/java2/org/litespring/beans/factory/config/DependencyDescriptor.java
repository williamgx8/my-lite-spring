package java2.org.litespring.beans.factory.config;

import java.lang.reflect.Field;

public class DependencyDescriptor {
    private final Field field;
    private final boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        this.field = field;
        this.required = required;
    }

    public Class getDependencyType() {
        return null;
    }
}
