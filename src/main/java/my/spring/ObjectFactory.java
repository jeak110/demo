package my.spring;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

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
        if (type.isInterface()) {
            return (T) map.get(type).newInstance();
        }
        return type.newInstance();
    }
}
