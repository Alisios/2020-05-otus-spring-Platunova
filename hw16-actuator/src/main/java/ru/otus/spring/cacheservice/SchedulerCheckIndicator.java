package ru.otus.spring.cacheservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerCheckIndicator implements HealthIndicator {

    private final HealthCheckService healthCheckService;

    @Override
    public Health health() {
        return healthCheckService.checkCacheSchedulerIndicator() ?
                Health.up()
                        .withDetail("time", "Последнее обновление кэша:  " + healthCheckService.getLastTimeOfUpdate())
                        .build() :
                Health.down()
                        .withDetail("message", "Обновление кэша не происходило в течении суток!")
                        .withDetail("time", "Последнее обновление кэша:  " + healthCheckService.getLastTimeOfUpdate())
                        .build();
    }

}