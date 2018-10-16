package java2.org.litespring.core.type.classreading;

import java2.org.litespring.core.io.Resource;
import java2.org.litespring.core.type.AnnotationMetadata;
import java2.org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;

public class SimpleMetadataReader implements MetadataReader {
    private final Resource resource;
    private final AnnotationMetadataReadingVisitor annotationMetadata;
    private final ClassMetadataReadingVisitor classMetadata;

    public SimpleMetadataReader(Resource resource) {
        this.resource = resource;
        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classMetadata = visitor;
        annotationMetadata = visitor;

        InputStream is = null;
        try {
            is = resource.getInputStream();

            ClassReader classReader = new ClassReader(is);
            classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {

            }
        }
    }

    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }
}
