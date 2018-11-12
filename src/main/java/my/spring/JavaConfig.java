package my.spring;

import java.util.HashMap;
import java.util.Map;

public class JavaConfig implements Config {
    private Map<Class, Class> map = new HashMap<>();

    public JavaConfig() {
        map.put(Cleaner.class, CleanerImpl.class);
        map.put(Speaker.class, ConsoleSpeaker.class);

    }

    @Override
    public <T> Class<T> getClassImpl(Class<T> type) {
        Class<T> classToCreate = type;
        if (type.isInterface()) {
            classToCreate = map.get(type);
        }
        return classToCreate;
    }
}
