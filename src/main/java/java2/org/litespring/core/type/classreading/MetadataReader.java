package java2.org.litespring.core.type.classreading;

import java2.org.litespring.core.io.Resource;
import java2.org.litespring.core.type.AnnotationMetadata;
import java2.org.litespring.core.type.ClassMetadata;

public interface MetadataReader {
    Resource getResource();

    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();
}
