package java2.org.litespring.beans.factory.annotation;

import java.util.Collection;
import java.util.List;

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

    public void inject(Object obj) {

    }

    public abstract static class InjectedElement {

    }
}
