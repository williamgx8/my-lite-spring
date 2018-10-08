package java2.org.litespring.beans;

import java2.org.litespring.beans.propertyeditors.CustomBooleanEditor;
import java2.org.litespring.beans.propertyeditors.CustomNumberEditor;
import java2.org.litespring.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

public class SimpleTypeConverter implements TypeConverter {
    private Map<Class<?>, PropertyEditor> defaultEditors;

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredClass) throws TypeMismatchException {
        if (ClassUtils.isAssignableValue(requiredClass, value)) {
            return (T) value;
        }

        if (value instanceof String) {
            PropertyEditor propertyEditor = findDefaultEditor(requiredClass);
            try {
                propertyEditor.setAsText((String) value);

            } catch (Exception e) {
                throw new TypeMismatchException(value, requiredClass);
            }

            return (T) propertyEditor.getValue();
        } else {
            throw new RuntimeException("cannot convert value " + value + " to class " + requiredClass);
        }

    }

    private <T> PropertyEditor findDefaultEditor(Class<T> requiredType) {
        PropertyEditor editor = this.getDefaultEditor(requiredType);

        if (editor == null) {
            throw new RuntimeException("Editor for " + requiredType + " has not been implemented");
        }

        return editor;
    }

    public PropertyEditor getDefaultEditor(Class<?> requiredType) {
        if (defaultEditors == null) {
            this.defaultEditors = new HashMap<>();
            createDefaultEditors();
        }
        return defaultEditors.get(requiredType);
    }

    private void createDefaultEditors() {
        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
        this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));
    }
}
