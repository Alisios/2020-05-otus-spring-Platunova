package ru.otus.spring.cacheservice;

import com.google.inject.internal.cglib.core.$MethodInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HealthCheckService {

    private final AtomicInteger schedulerHealthIndicator = new AtomicInteger(0);
    private final Map<String, String> timeMap = new ConcurrentHashMap<>();
    static private final String KEY = "time";

    public void addAtomicHealth() {
        schedulerHealthIndicator.incrementAndGet();
        timeMap.put(KEY, LocalDateTime.now().toString());
    }

    public boolean checkCacheSchedulerIndicator() {
        boolean result = schedulerHealthIndicator.get() > 0;
        schedulerHealthIndicator.set(0);
        return result;
    }

    public String getLastTimeOfUpdate() {
        return timeMap.getOrDefault(KEY, "Кэш ни разу не обновлялся");
    }

}
