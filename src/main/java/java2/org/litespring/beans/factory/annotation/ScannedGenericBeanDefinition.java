package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.factory.support.GenericBeanDefinition;
import java2.org.litespring.core.type.AnnotationMetadata;
import java2.org.litespring.core.type.classreading.MetadataReader;

public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {
    private final AnnotationMetadata annotationMetadata;

    public ScannedGenericBeanDefinition(MetadataReader metadataReader) {
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        this.annotationMetadata = annotationMetadata;
        setBeanClassName(this.annotationMetadata.getClassName());
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return this.annotationMetadata;
    }
}
