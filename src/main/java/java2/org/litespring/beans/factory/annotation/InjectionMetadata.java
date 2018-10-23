package java2.org.litespring.beans.factory.annotation;

import java2.org.litespring.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;
import java.util.Collection;

public class InjectionMetadata {
    private final Class targetClass;
    private final Collection<InjectedElement> injectedElements;

    public InjectionMetadata(Class targetClass, Collection injectedElements) {
        this.targetClass = targetClass;
        this.injectedElements = injectedElements;

    }

    public Collection<InjectedElement> getInjectedElements() {

        return this.injectedElements;
    }

    public void inject(Object target) throws IllegalAccessException {
        if (injectedElements == null || injectedElements.isEmpty()) {
            return;
        }

        for (InjectedElement injectedElement : injectedElements) {
            injectedElement.inject(target);
        }

    }

    /**
     * 每一个注入元素的基类
     */
    public abstract static class InjectedElement {
        protected Member member;
        protected AutowireCapableBeanFactory factory;

        InjectedElement(Member member, AutowireCapableBeanFactory factory) {
            this.member = member;
            this.factory = factory;
        }

        public abstract void inject(Object target) throws IllegalAccessException;

    }
}
