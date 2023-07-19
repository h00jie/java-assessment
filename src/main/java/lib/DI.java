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

    public <T> T oneOf(Class<T> clazz) {
        return createInstance(clazz);
    }

    public <T> T singletonOf(Class<T> clazz) {
        if (singletonInstances.containsKey(clazz)) {
            return (T) singletonInstances.get(clazz);
        }
        T instance = createInstance(clazz);
        singletonInstances.put(clazz, instance);
        return instance;
    }

    public <T> List<T> listOf(Class<T> clazz) {
        List<T> instances = new ArrayList<>();
        String packageName = "gr.pricefox.implementions";
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        for (Class<? extends T> implementor : reflections.getSubTypesOf(clazz)) {
            try {
                instances.add(implementor.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instances;
    }

    private <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
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
            throw new RuntimeException("Cannot create instance of " + clazz.getSimpleName());
        }
    }

    private <T> void injectDependencies(T instance) {
        Class<?> clazz = instance.getClass();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            Class<?> dependencyClass = field.getType();
            Object dependencyInstance = createInstance(dependencyClass);
            try {
                field.setAccessible(true);
                field.set(instance, dependencyInstance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject dependency for " + clazz.getSimpleName());
            }
        }
    }

}
