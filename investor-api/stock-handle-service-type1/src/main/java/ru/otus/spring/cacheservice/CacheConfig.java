package ru.otus.spring.cacheservice;

import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.models.CacheStockInfo;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final MessageProperties properties;

    @Bean
    ConcurrentMap<Object, Object> yesterdayInfoCache() {
        ConcurrentMap<Object, Object> map = CacheBuilder.newBuilder()
                .expireAfterWrite(properties.getCacheLifeTime(), TimeUnit.HOURS)
                .build()
                .asMap();

        //--------Временная инициализация----------------
        map.put("sber", CacheStockInfo.builder().ticker("sber").high(400).close(400).date(LocalDateTime.now()).low(50).open(50).build());
        //-----------------------------
        return map;
    }
}
