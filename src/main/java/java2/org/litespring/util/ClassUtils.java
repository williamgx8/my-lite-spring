package java2.org.litespring.util;

import java.util.IdentityHashMap;
import java.util.Map;

public class ClassUtils {
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);

    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
        primitiveWrapperTypeMap.forEach((key, value) -> primitiveTypeToWrapperMap.put(value, key));
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        Assert.notNull(lhsType, "Left-hand side type must not be null");
        Assert.notNull(rhsType, "Right-hand side type must not be null");
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            if (lhsType == resolvedPrimitive) {
                return true;
            }
        } else {
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAssignableValue(Class<?> type, Object value) {
        Assert.notNull(type, "Type must not be null");
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
    }

    public static String convertClassNameToResourcePath(String className) {
        Assert.notNull(className, "Class name must not be null");
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    public static String convertResourcePathToClassName(String resourcePath) {
        Assert.notNull(resourcePath, "Resource path must not be null");
        return resourcePath.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
    }
}
