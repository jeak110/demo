package my.spring;

import java.util.List;

public interface Config {
    <T> Class<T> getClassImpl(Class<T> type);
    List<String> packagesToScan();
}
