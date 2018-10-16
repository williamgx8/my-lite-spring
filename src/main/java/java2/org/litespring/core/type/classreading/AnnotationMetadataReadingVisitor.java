package java2.org.litespring.core.type.classreading;

import java2.org.litespring.core.annotation.AnnotationAttributes;
import java2.org.litespring.core.type.AnnotationMetadata;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 这里需要继承ClassMetadataReadingVisitor的原因是，AnnotationMetadataReadingVisitor实现的AnnotationMetadata接口继承了
 * ClassMetadata，ClassMetadata中很多方法已经在ClassMetadataReadingVisitor中实现过了，继承它就无需再重复实现一遍。实际上
 * Annotation可以认为是一种特殊的Class
 */
public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {
    /**
     * 当前类上所加全部注解对应的class全路径名
     */
    private final Set<String> annotationSet = new LinkedHashSet<>();

    /**
     * key:类上注解的全路径名称，value:每条注解包含的所有属性
     */
    private final Map<String, AnnotationAttributes> attributesMap = new LinkedHashMap<>();

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, boolean visitable) {
        String className = Type.getType(desc).getClassName();
        this.annotationSet.add(className);
        //某个注解中所有的属性由AnnotationAttributesReadingVisitor解析，并放入attributesMap中
        return new AnnotationAttributesReadingVisitor(className, attributesMap);
    }

    @Override
    public Set<String> getAnnotationTypes() {
        return this.annotationSet;
    }

    @Override
    public boolean hasAnnotation(String annotationType) {
        return this.annotationSet.contains(annotationType);
    }

    @Override
    public AnnotationAttributes getAnnotationAttributes(String annotationType) {
        return this.attributesMap.get(annotationType);
    }
}
