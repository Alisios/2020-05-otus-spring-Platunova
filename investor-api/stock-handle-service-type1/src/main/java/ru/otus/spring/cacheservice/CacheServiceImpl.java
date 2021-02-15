package ru.otus.spring.cacheservice;

import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.eventservice.EventTypeService;
import ru.otus.spring.handleservice.eventservice.EventTypeServiceChange;
import ru.otus.spring.handleservice.eventservice.EventTypeServiceMax;
import ru.otus.spring.handleservice.eventservice.EventTypeServiceMin;
import ru.otus.spring.handleservice.models.CacheStockInfo;
import ru.otus.spring.handleservice.models.StockInfoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final MessageProperties properties;
    private final StockInfoMapper mapper;

    private final ConcurrentMap<Object, Object> cacheBuilder =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(24, TimeUnit.HOURS)
                    .build()
                    .asMap();

    private final ConcurrentHashMap<String, EventTypeService> innerHandlerMap = new ConcurrentHashMap<>();
    private final AtomicBoolean key = new AtomicBoolean(false);

    @Override
    public void updateInfo(List<CacheStockInfo> listOfInfo) {
        Map<String, CacheStockInfo> map = listOfInfo
                .stream()
                .collect(Collectors.toConcurrentMap(CacheStockInfo::getTicker,
                        Function.identity()));
        cacheBuilder.putAll(map);
    }

    @Override
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

    @Override
    public ConcurrentHashMap<String, EventTypeService> getHandlerMap() {
        if (key.compareAndSet(false, true))
            initiateHandlers();
        return innerHandlerMap;
    }

    private void initiateHandlers() {
        innerHandlerMap.put("event_1", new EventTypeServiceChange(mapper, properties));
        innerHandlerMap.put("event_2", new EventTypeServiceMax(mapper, properties, this));
        innerHandlerMap.put("event_3", new EventTypeServiceMin(mapper, properties, this));
    }
}
