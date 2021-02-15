package ru.otus.spring.cacheservice;

import ru.otus.spring.handleservice.eventservice.EventTypeService;
import ru.otus.spring.handleservice.models.CacheStockInfo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Интерфейс сервиса, создающего кэш и
 * обновляющий его раз в сутки по приходу события из сервиса stock-info
 */
public interface CacheService {

    /**
     * метод предназначен для обновления значений кэша  (инофрмации по акциям)
     *
     * @param listOfInfo - список значений для обновления
     */
    void updateInfo(List<CacheStockInfo> listOfInfo);

    /**
     * метод предназначен для получения доступа к кэшу
     *
     * @return получение кэша
     */
    ConcurrentMap<Object, Object> getCacheBuilder();

    /**
     * метод предназначен для получения обработчиков бизнес событий
     *
     * @return мапа из типа события и его обработчика
     */
    ConcurrentHashMap<String, EventTypeService> getHandlerMap();
}
