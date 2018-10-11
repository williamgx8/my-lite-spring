package java2.org.litespring.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

public class ConstructorArgumentValues {
    private List<ValueHolder> valueHolders = new ArrayList<>();

    public List<ValueHolder> getArgumentValues() {
        return valueHolders;
    }

    public void addArgumentValue(ValueHolder valueHolder) {
        this.valueHolders.add(valueHolder);
    }

    public static class ValueHolder {

        private Object value;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    public boolean isEmpty() {
        return valueHolders.isEmpty();
    }
}

