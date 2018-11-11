package my.spring;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ObjectFactory {
    private static ObjectFactory instance;
    private Map<Class, Class> map = new HashMap<>();


    public static ObjectFactory getInstance() {
        return instance == null ? instance = new ObjectFactory() : instance;
    }

    public ObjectFactory() {
        map.put(Cleaner.class, CleanerImpl.class);
        map.put(Speaker.class, ConsoleSpeaker.class);
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        Class<T> classToCreate = type;
        if (type.isInterface()) {
            classToCreate = map.get(type);
        }

        T t = classToCreate.newInstance();

        // random inject
        for (Field field : classToCreate.getDeclaredFields()) {
            InjectRandomInt injectRandomInt = field.getAnnotation(InjectRandomInt.class);
            if (injectRandomInt != null) {
                int min = injectRandomInt.min();
                int max = injectRandomInt.max();
                field.setAccessible(true);
                field.set(t, ThreadLocalRandom.current().nextInt(min, max));
            }
        }

        return t;
    }
}
