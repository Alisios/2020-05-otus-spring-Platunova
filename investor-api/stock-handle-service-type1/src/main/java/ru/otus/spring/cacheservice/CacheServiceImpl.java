package ru.otus.spring.cacheservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.handleservice.models.CacheStockInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final ConcurrentMap<Object, Object> yesterdayInfoCache;
    private final HealthCheckService healthCheckService;

    @Override
    public void updateInfo(List<CacheStockInfo> listOfInfo) {
        Map<String, CacheStockInfo> map = listOfInfo
                .stream()
                .collect(Collectors.toConcurrentMap(CacheStockInfo::getTicker,
                        Function.identity()));
        yesterdayInfoCache.putAll(map);
        healthCheckService.addAtomicHealth();
    }

    @Override
    public ConcurrentMap<Object, Object> getCache() {
        return yesterdayInfoCache;
    }

}
