package my.spring;

public interface Config {
    <T> Class<T> getClassImpl(Class<T> type);
}
