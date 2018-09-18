package java2.org.litespring.beans;

public class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
