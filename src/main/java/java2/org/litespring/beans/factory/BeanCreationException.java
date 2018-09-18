package java2.org.litespring.beans.factory;

import java2.org.litespring.beans.BeansException;

public class BeanCreationException extends BeansException {
    private String beanName;
    public BeanCreationException(String msg) {
        super(msg);
    }

    public BeanCreationException(String msg, Throwable ex) {
        super(msg, ex);
    }


    public BeanCreationException(String beanName, String msg) {
        super("Error creating bean with name '" + beanName + "': " + msg);
        this.beanName = beanName;
    }

    public BeanCreationException(String beanName, String msg, Throwable cause) {
        this(beanName, msg);
        initCause(cause);
    }

    public String getBeanName() {
        return beanName;
    }
}
