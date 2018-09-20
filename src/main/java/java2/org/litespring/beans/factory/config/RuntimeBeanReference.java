package java2.org.litespring.beans.factory.config;

public class RuntimeBeanReference {

    private final String beanName;
    private final boolean toParent;

    public RuntimeBeanReference(String beanName) {
        this(beanName, false);
    }

    public RuntimeBeanReference(String beanName, boolean toParent) {
        this.beanName = beanName;
        this.toParent = toParent;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
