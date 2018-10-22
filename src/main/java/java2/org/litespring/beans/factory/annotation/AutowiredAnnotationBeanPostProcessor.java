package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.BeansException;
import java2.org.litespring.beans.factory.BeanFactory;
import java2.org.litespring.beans.factory.BeanFactoryAware;
import java2.org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import java2.org.litespring.core.annotation.AnnotationUtils;
import java2.org.litespring.util.ReflectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();
    private static final Log logger = LogFactory.getLog(AutowiredAnnotationBeanPostProcessor.class);
    private String requiredParameterName = "required";
    private boolean requiredParameterValue = true;

    public AutowiredAnnotationBeanPostProcessor() {
        autowiredAnnotationTypes.add(Autowired.class);
    }

    @Override
    public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
        return false;
    }

    @Override
    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public InjectionMetadata buildAutowiringMetadata(Class targetClass) {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        ReflectionUtils.doWithLocalFields(targetClass, field -> {
            Annotation annotation = findAutowiredAnnotation(field);
            if (Modifier.isStatic(field.getModifiers())) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Autowired annotation is not supported on static fields: " + field);
                }
            }
            return;
        });
        return new InjectionMetadata(targetClass, elements);
    }

    private Annotation findAutowiredAnnotation(Field field) {
        for (Class<? extends Annotation> autowiredAnnotationType : this.autowiredAnnotationTypes) {
            Annotation annotation = AnnotationUtils.getAnnotation(field, autowiredAnnotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    protected boolean determineRequiredStatus(Annotation ann) {
        Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
        if (method == null) {
            return true;
        }
        return this.requiredParameterValue == (Boolean)ReflectionUtils.invokeMethod(method, ann);
    }

}
