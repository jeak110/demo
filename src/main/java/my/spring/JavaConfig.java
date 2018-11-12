package my.spring;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

public class JavaConfig implements Config {
    private Reflections scanner;
    private Map<Class, Class> map = new HashMap<>();

    @SneakyThrows
    public JavaConfig() {
        scanner = new Reflections(packagesToScan());
        map.put(Speaker.class, PopupSpeaker.class);
    }

    @Override
    public <T> Class<T> getClassImpl(Class<T> type) {
        Class<T> classToCreate = type;
        if (type.isInterface()) {
            classToCreate = map.get(type);
            if (classToCreate == null) {
                Set<Class<? extends T>> subTypes = scanner.getSubTypesOf(type);
                if (subTypes.size() == 1) {
                    classToCreate = (Class<T>) subTypes.iterator().next();
                } else {
                    throw new RuntimeException("Zero or more implementation found for " + type
                            + ". Check your configuration");
                }
            }
        }
        return classToCreate;
    }

    @Override
    public List<String> packagesToScan() {
        return asList("my.spring");
    }
}
