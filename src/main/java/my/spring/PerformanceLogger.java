package my.spring;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PerformanceLogger {
    private static PerformanceLogger instance;
    public static PerformanceLogger getInstance() {
        return instance == null ? instance = new PerformanceLogger() : instance;
    }

    public void logMethodStart(String methodName) {
        System.out.println(methodName +
                " started at " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) +
                " Memory max = " + Runtime.getRuntime().maxMemory() + " / free = " + Runtime.getRuntime().freeMemory());
    }

    public void logMethodEnd(String methodName) {
        System.out.println(methodName +
                " ended at " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) +
                " Memory max = " + Runtime.getRuntime().maxMemory() + " / free = " + Runtime.getRuntime().freeMemory());
    }
}
