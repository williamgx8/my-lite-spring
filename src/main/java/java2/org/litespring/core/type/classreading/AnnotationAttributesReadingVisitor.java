package java2.org.litespring.core.type.classreading;

import java2.org.litespring.core.annotation.AnnotationAttributes;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.SpringAsmInfo;

import java.util.Map;

public class AnnotationAttributesReadingVisitor extends AnnotationVisitor {

    //当前注解的全路径名称
    private final String annotationType;

    private final Map<String, AnnotationAttributes> attributesMap;

    private AnnotationAttributes annotationAttributes = new AnnotationAttributes();

    public AnnotationAttributesReadingVisitor(String annotationType, Map<String, AnnotationAttributes> attributesMap) {
        super(SpringAsmInfo.ASM_VERSION);
        this.annotationType = annotationType;
        this.attributesMap = attributesMap;
    }

    @Override
    public void visitEnd() {
        this.attributesMap.put(this.annotationType, this.annotationAttributes);
    }

    @Override
    public void visit(String attributeName, Object attributeValue) {
        this.annotationAttributes.put(attributeName, attributeValue);
    }
}
