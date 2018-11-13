package my.spring;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {
    private static ObjectFactory instance;
    private Config config = new JavaConfig();
    private List<ObjectConfigurator> configurators = new ArrayList<>();
    private List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();
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
        for (Class<? extends ProxyConfigurator> configuratorClass : scanner.getSubTypesOf(ProxyConfigurator.class)) {
            proxyConfigurators.add(configuratorClass.newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        Class<T> classToCreate = config.getClassImpl(type);
        T t = classToCreate.newInstance();
        configure(t);
        invokeInitMethods(t);
        t = wrapProxyIfNeeded(t);
        return t;
    }

    private <T> T wrapProxyIfNeeded(T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = (T) proxyConfigurator.wrapWithProxy(t);
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
