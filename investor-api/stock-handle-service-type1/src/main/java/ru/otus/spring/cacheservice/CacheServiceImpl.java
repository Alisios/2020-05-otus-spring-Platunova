package ru.otus.spring.cacheservice;

import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.models.CacheStockInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CacheServiceImpl implements CacheService {

    MessageProperties properties;

    private final ConcurrentMap<Object, Object> cacheBuilder =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(24, TimeUnit.HOURS)//properties.getCacheLifeTime(), TimeUnit.HOURS)
                    .build()
                    .asMap();


    public void updateInfo(List<CacheStockInfo> listOfInfo) {
        Map<String, CacheStockInfo> map = listOfInfo
                .stream()
                .collect(Collectors.toConcurrentMap(CacheStockInfo::getTicker,
                        Function.identity()));
        cacheBuilder.putAll(map);
    }


    public ConcurrentMap<Object, Object> getCacheBuilder() {
        cacheBuilder.put("sber", CacheStockInfo.builder() //Временно!
                .ticker("sber")
                .high(400)
                .close(400)
                .date(LocalDateTime.now())
                .low(50)
                .open(50)
                .build());
        return cacheBuilder;
    }
}
