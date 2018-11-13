package my.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LogPerformanceProxyConfigurator implements ProxyConfigurator {
    @Override
    public Object wrapWithProxy(Object t) {
        boolean annotationOnMethod = Arrays.stream(t.getClass().getDeclaredMethods())
                .anyMatch(m->m.isAnnotationPresent(LogPerformance.class));

        if (t.getClass().isAnnotationPresent(LogPerformance.class) || annotationOnMethod) {
            return Proxy.newProxyInstance(t.getClass().getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return wrapMethod(method, args, t);
                }
            });
        }
        return t;
    }

    private Object wrapMethod(Method method, Object[] args, Object t) throws IllegalAccessException, InvocationTargetException {
        if (t.getClass().isAnnotationPresent(LogPerformance.class) || method.isAnnotationPresent(LogPerformance.class)) {
            PerformanceLogger.getInstance().logMethodStart(method.getName());
            Object invoke = method.invoke(t, args);
            PerformanceLogger.getInstance().logMethodEnd(method.getName());
            return invoke;
        }
        return method.invoke(t, args);
    }
}
