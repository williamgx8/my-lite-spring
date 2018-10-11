package java2.org.litespring.beans.factory.support;

import java2.org.litespring.beans.BeanDefinition;
import java2.org.litespring.beans.SimpleTypeConverter;
import java2.org.litespring.beans.TypeMismatchException;
import java2.org.litespring.beans.factory.BeanCreationException;
import java2.org.litespring.beans.factory.BeanFactory;
import java2.org.litespring.beans.factory.config.ConstructorArgumentValues;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorResolver {
    private final BeanFactory beanFactory;

    public ConstructorResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(BeanDefinition bd) {
        ConstructorArgumentValues constructorArgumentValues = bd.getConstructorArgumentValues();
        if (constructorArgumentValues.isEmpty()) {
            return null;
        }

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getArgumentValues();
        String beanClassName = bd.getBeanClassName();
        Object[] argsToUse = null;
        Constructor constructorToUse = null;

        Class<?> clazz = null;
        try {
            clazz = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException("cannot found class, class name : " + beanClassName, e);
        }

        Constructor<?>[] candidates = clazz.getConstructors();
        for (Constructor<?> constructor : candidates) {
            int parameterCount = constructor.getParameterCount();
            if (parameterCount != argumentValues.size()) {
                continue;
            }

            Class<?>[] parameterTypes = constructor.getParameterTypes();

            boolean matchConstructor = matchConstructor(argumentValues, parameterTypes);
            if (!matchConstructor) {
                continue;
            }
            constructorToUse = constructor;
            argsToUse = resolveToArguments(argumentValues, parameterTypes);

            try {
                return constructorToUse.newInstance(argsToUse);
            } catch (Exception e) {
                throw new BeanCreationException("");
            }
        }
        throw new BeanCreationException("");
    }

    private boolean matchConstructor(List<ConstructorArgumentValues.ValueHolder> argumentValues, Class<?>[] parameterTypes) {
        BeanDefinitionValueResolver beanDefinitionValueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        for (int i = 0; i < argumentValues.size(); i++) {
            Object resolvedValue = beanDefinitionValueResolver.resolveValueIfNecessary(argumentValues.get(i).getValue());
            try {
                simpleTypeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
            } catch (TypeMismatchException e) {
                //转换失败说明这个不匹配
                return false;
            }
        }
        return true;
    }

    private Object[] resolveToArguments(List<ConstructorArgumentValues.ValueHolder> argumentValues, Class<?>[] parameterTypes) {
        BeanDefinitionValueResolver beanDefinitionValueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < argumentValues.size(); i++) {
            Object originalValue = argumentValues.get(i).getValue();
            Object resolvedValue = beanDefinitionValueResolver.resolveValueIfNecessary(originalValue);
            Object convertedValue = simpleTypeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
            arguments[i] = convertedValue;
        }

        return arguments;
    }
}
