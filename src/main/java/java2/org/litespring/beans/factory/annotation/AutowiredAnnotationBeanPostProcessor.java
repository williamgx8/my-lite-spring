package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.BeansException;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.BeanFactory;
import java2.org.litespring.beans.factory.BeanFactoryAware;
import java2.org.litespring.beans.factory.config.AutowireCapableBeanFactory;
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

    private AutowireCapableBeanFactory beanFactory;
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
        return true;
    }

    @Override
    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata injectionMetadata = buildAutowiringMetadata(bean.getClass());
        try {
            injectionMetadata.inject(bean);
        } catch (IllegalAccessException e) {
            throw new BeanCreationException("beanName, Injection of autowired dependencies failed", e);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof AutowireCapableBeanFactory)) {
            throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }

    /**
     * 遍历field调用钩子方法，得到匹配autowiredAnnotationTypes的Annotation，检测Annotation的required属性，
     * 封装成AutowiredFieldElement，将类中所有@Autowired修饰的字段，每一个修饰的字段封装成一个AutowiredFieldElement
     *
     * @param targetClass 存在注解注入的类
     * @return
     */
    public InjectionMetadata buildAutowiringMetadata(Class targetClass) {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        ReflectionUtils.doWithLocalFields(targetClass, field -> {
            Annotation annotation = findAutowiredAnnotation(field);

            if (annotation == null) {
                return;
            }

            if (Modifier.isStatic(field.getModifiers())) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Autowired annotation is not supported on static fields: " + field);
                }
            }

            boolean required = determineRequiredStatus(annotation);
            elements.add(new AutowiredFieldElement(field, required, this.beanFactory));
        });
        return new InjectionMetadata(targetClass, elements);
    }

    private Annotation findAutowiredAnnotation(Field field) {
        for (Class<? extends Annotation> autowiredAnnotationType : this.autowiredAnnotationTypes) {
            /**
             * AnnotationUtils.getAnnotation(field, autowiredAnnotationType)
             * 相当于field.getAnnotation(autowiredAnnotationType)
             * 获得属性上指定的注解
             */
            Annotation annotation = AnnotationUtils.getAnnotation(field, autowiredAnnotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 这里相当于直接ann.required()，抽取成公用的，接受基类Annotation
     *
     * @param ann
     * @return
     */
    protected boolean determineRequiredStatus(Annotation ann) {
        Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
        if (method == null) {
            return true;
        }
        return this.requiredParameterValue == (Boolean) ReflectionUtils.invokeMethod(method, ann);
    }

}
