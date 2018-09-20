package java2.org.litespring.beans;

import java2.org.litespring.util.Assert;

public class PropertyValue {
    private final String name;
    private final Object value;

    public PropertyValue(String name, Object value) {
        Assert.notNull(name, "name cannot be null!");
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return value;
    }
}
