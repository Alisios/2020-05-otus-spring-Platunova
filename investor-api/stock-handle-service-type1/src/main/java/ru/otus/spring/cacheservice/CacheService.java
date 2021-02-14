package ru.otus.spring.cacheservice;

import ru.otus.spring.handleservice.models.CacheStockInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Интерфейс сервиса, создающего кэш и
 * обновляющий его раз в сутки по приходу события из сервиса stock-info
 */
public interface CacheService {

    /**
     * метод предназначен для обновления значений кэша  (инофрмации по акциям)
     * @param listOfInfo - список значений для обновления
     */
    void updateInfo(List<CacheStockInfo> listOfInfo);

    /**
     * метод предназначен для получения доступа к кэшу
     * @return
     */
    ConcurrentMap<Object, Object> getCacheBuilder();
}
