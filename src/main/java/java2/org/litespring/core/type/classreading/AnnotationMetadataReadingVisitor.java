package java2.org.litespring.core.type.classreading;

import java2.org.litespring.core.annotation.AnnotationAttributes;
import java2.org.litespring.core.type.AnnotationMetadata;

import java.util.Set;

public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {
    @Override
    public Set<String> getAnnotationTypes() {
        return null;
    }

    @Override
    public boolean hasAnnotation(String annotationType) {
        return false;
    }

    @Override
    public AnnotationAttributes getAnnotationAttributes(String annotationType) {
        return null;
    }
}
