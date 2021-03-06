package java2.org.litespring.beans;

import java2.org.litespring.util.Assert;

public class PropertyValue {
    private final String name;
    private final Object value;

    private boolean converted = false;
    private Object convertedValue;

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

    public synchronized boolean isConverted() {
        return this.converted;
    }

    public synchronized void setConvertedValue(Object value) {
        this.converted = true;
        this.convertedValue = value;
    }

    public synchronized Object getConvertedValue() {
        return this.convertedValue;
    }
}
