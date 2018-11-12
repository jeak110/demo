package my.spring;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {
    private static ObjectFactory instance;
    private Config config = new JavaConfig();
    private List<ObjectConfigurator> configurators = new ArrayList<>();

    public static ObjectFactory getInstance() {
        return instance == null ? instance = new ObjectFactory() : instance;
    }

    public ObjectFactory() {
        configurators.add(new InjectRandomIntObjectConfigurator());
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
