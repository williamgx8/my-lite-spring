package java2.org.litespring.context.annotation;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.annotation.ScannedGenericBeanDefinition;
import java2.org.litespring.beans.factory.support.BeanDefinitionRegistry;
import java2.org.litespring.beans.factory.support.BeanNameGenerator;
import java2.org.litespring.core.io.Resource;
import java2.org.litespring.core.io.support.PackageResourceLoader;
import java2.org.litespring.core.type.classreading.MetadataReader;
import java2.org.litespring.core.type.classreading.SimpleMetadataReader;
import java2.org.litespring.stereotype.Component;
import java2.org.litespring.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner {
    private final BeanDefinitionRegistry registry;
    private PackageResourceLoader resourceLoader = new PackageResourceLoader();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String packagesToScan) {

        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (String basePackage : StringUtils.tokenizeToStringArray(packagesToScan, ",")) {
            try {
                Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
                beanDefinitions.addAll(candidateComponents);
            } catch (IOException e) {
                throw new BeanCreationException("scan package " + packagesToScan + "error", e);
            }

        }

        for (BeanDefinition beanDefinition : beanDefinitions) {
            registry.registerBeanDefinition(beanDefinition.getID(), beanDefinition);
        }
    }

    private Set<BeanDefinition> findCandidateComponents(String basePackage) throws IOException {
        Set<BeanDefinition> beanDefinitionSet = new HashSet<>();

        Resource[] resources = resourceLoader.getResources(basePackage);

        for (Resource resource : resources) {
            MetadataReader metadataReader = new SimpleMetadataReader(resource);
            if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                ScannedGenericBeanDefinition scannedGenericBeanDefinition = new ScannedGenericBeanDefinition(metadataReader);
                String id = this.beanNameGenerator.generateBeanName(scannedGenericBeanDefinition, this.registry);
                scannedGenericBeanDefinition.setID(id);
                beanDefinitionSet.add(scannedGenericBeanDefinition);
            }
        }
        return beanDefinitionSet;
    }
}
