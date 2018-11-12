package my.spring;

import lombok.SneakyThrows;
import org.reflections.Reflections;

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

        return t;
    }

    private <T> void configure(T t) {
        configurators.forEach(x -> x.configure(t));
    }
}
