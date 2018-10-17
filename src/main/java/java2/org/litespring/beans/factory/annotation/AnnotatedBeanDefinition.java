package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.core.type.AnnotationMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {

    AnnotationMetadata getMetadata();

}
