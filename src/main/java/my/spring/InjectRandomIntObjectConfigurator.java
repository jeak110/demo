package my.spring;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

public class InjectRandomIntObjectConfigurator implements ObjectConfigurator {
    @SneakyThrows
    @Override
    public void configure(Object t) {
        // random inject
        for (Field field : t.getClass().getDeclaredFields()) {
            InjectRandomInt injectRandomInt = field.getAnnotation(InjectRandomInt.class);
            if (injectRandomInt != null) {
                int min = injectRandomInt.min();
                int max = injectRandomInt.max();
                field.setAccessible(true);
                field.set(t, ThreadLocalRandom.current().nextInt(min, max));
            }
        }
    }
}
