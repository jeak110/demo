package my.spring;

public interface ProxyConfigurator {
    Object wrapWithProxy(Object t, Class type);
}
