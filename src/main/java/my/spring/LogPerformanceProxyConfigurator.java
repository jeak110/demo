package my.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LogPerformanceProxyConfigurator implements ProxyConfigurator {
    @Override
    public Object wrapWithProxy(Object t, Class type) {
        boolean annotationOnMethod = Arrays.stream(type.getDeclaredMethods())
                .anyMatch(m->m.isAnnotationPresent(LogPerformance.class));

        if (type.isAnnotationPresent(LogPerformance.class) || annotationOnMethod) {
            return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Method classMethod = type.getMethod(method.getName(), method.getParameterTypes());
                    return wrapMethod(type, method, classMethod, t, args);
                }
            });
        }
        return t;
    }

    private Object wrapMethod(Class type, Method invokeMethod, Method classMethod, Object t, Object[] args) throws IllegalAccessException, InvocationTargetException {
        if (type.isAnnotationPresent(LogPerformance.class) || classMethod.isAnnotationPresent(LogPerformance.class)) {
            PerformanceLogger.getInstance().logMethodStart(invokeMethod.getName());
            Object invoke = invokeMethod.invoke(t, args);
            PerformanceLogger.getInstance().logMethodEnd(invokeMethod.getName());
            return invoke;
        }
        return invokeMethod.invoke(t, args);
    }
}
