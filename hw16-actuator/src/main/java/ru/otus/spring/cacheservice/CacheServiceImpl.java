package ru.otus.spring.cacheservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.stockinfoservice.database.dto.CacheStockInfo;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("cacheService")
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final ConcurrentMap<Object, Object> yesterdayInfoCache;
    private final HealthCheckService healthCheckService;

    @Override
    public void updateInfo(List<CacheStockInfo> listOfInfo) {
        log.info("Обновление кэша {}", LocalDateTime.now());
        if (listOfInfo.isEmpty()){
            log.error("Список для обновления кэша пустой!");
            return;
        }
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
