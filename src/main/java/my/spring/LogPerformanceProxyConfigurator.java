package my.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogPerformanceProxyConfigurator implements ProxyConfigurator {
    @Override
    public Object wrapWithProxy(Object t) {
        if (t.getClass().isAnnotationPresent(LogPerformance.class)) {
            return Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    PerformanceLogger.getInstance().logMethodStart(method.getName());
                    Object invoke = method.invoke(t, args);
                    PerformanceLogger.getInstance().logMethodEnd(method.getName());
                    return invoke;
                }
            });
        }
        return t;
    }
}
