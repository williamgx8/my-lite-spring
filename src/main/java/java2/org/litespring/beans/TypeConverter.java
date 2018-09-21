package java2.org.litespring.beans;

public interface TypeConverter {

    <T> T convertIfNecessary(Object value, Class<T> requiredClass) throws TypeMismatchException;
}
