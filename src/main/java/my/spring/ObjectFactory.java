package my.spring;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {
    private static ObjectFactory instance;
    private Config config = new JavaConfig();
    private List<ObjectConfigurator> configurators = new ArrayList<>();
    private Reflections scanner;

    public static ObjectFactory getInstance() {
        return instance == null ? instance = new ObjectFactory() : instance;
    }

    @SneakyThrows
    public ObjectFactory() {
        scanner = new Reflections(config.packagesToScan());
        for (Class<? extends ObjectConfigurator> configuratorClass : scanner.getSubTypesOf(ObjectConfigurator.class)) {
            configurators.add(configuratorClass.newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        Class<T> classToCreate = config.getClassImpl(type);
        T t = classToCreate.newInstance();
        configure(t);
        invokeInitMethods(t);

        if (t.getClass().isAnnotationPresent(LogPerformance.class)) {
            return (T) Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    PerformanceLogger.getInstance().logMethodStart(method.getName());
                    Object invoke = method.invoke(t, args);
                    PerformanceLogger.getInstance().logMethodEnd(method.getName());
                    return invoke;
                }
            });
        }


        return t;
    }

    @SneakyThrows
    private <T> void invokeInitMethods(T t) {
        for (Method method : t.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(t, null);
            }
        }
    }

    private <T> void configure(T t) {
        configurators.forEach(x -> x.configure(t));
    }
}
