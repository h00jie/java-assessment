package lib;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DI {
    private final Map<Class<?>, Object> singletonInstances = new HashMap<>();

    public <T> T oneOf(Class<T> cls) {
        return createInstance(cls);
    }

    @SuppressWarnings("unchecked")
    public <T> T singletonOf(Class<T> cls) {
        if (singletonInstances.containsKey(cls)) {
            return (T) singletonInstances.get(cls);
        }
        T instance = createInstance(cls);
        singletonInstances.put(cls, instance);
        return instance;
    }

    public <T> List<T> listOf(Class<T> cls) {
        List<T> instances = new ArrayList<>();
        String packageName = "gr.pricefox.implementions";
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        for (Class<? extends T> implementor : reflections.getSubTypesOf(cls)) {
            try {
                instances.add(implementor.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instances;
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<T> cls) {
        try {
            Constructor<?>[] constructors = cls.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];
                boolean allDependenciesResolved = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    dependencies[i] = parameterTypes[i].getDeclaredConstructor().newInstance();
                    if (dependencies[i] == null) {
                        allDependenciesResolved = false;
                        break;
                    }
                }
                if (allDependenciesResolved) {
                    return (T) constructor.newInstance(dependencies);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of " + cls.getSimpleName());
        }
    }

}
