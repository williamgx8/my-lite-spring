package java2.org.litespring.core.type;

import java2.org.litespring.core.annotation.AnnotationAttributes;

import java.util.Set;

public interface AnnotationMetadata extends ClassMetadata {
    Set<String> getAnnotationTypes();

    boolean hasAnnotation(String annotationType);

    public AnnotationAttributes getAnnotationAttributes(String annotationType);
}
