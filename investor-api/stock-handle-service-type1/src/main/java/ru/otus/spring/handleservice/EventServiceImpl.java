package ru.otus.spring.handleservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.cacheservice.CacheService;
import ru.otus.spring.configuration.EventTypeProperties;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.eventservice.EventTypeService;
import ru.otus.spring.handleservice.models.CacheStockInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoMapper;
import ru.otus.spring.handleservice.models.StockInfoRes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * бизнес логика (пока простейшая)
 * У информации типа "акция" 3 типа собтия - увеличение цены,
 * увеличение текущей цены по сравнению с максимумом предыдущего дня,
 * уменьшение текущей цены по сравнению с минимумом цены предыдущего дня
 */
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    Map<String, EventTypeService> handlerMap = new HashMap<>();

    EventServiceImpl(List<EventTypeService> handlers) {
        handlers.forEach(h -> handlerMap.put(h.getType(), h));
    }

    @Override
    public List<StockInfoRes> getResult(StockInfoFull info) {
        return handlerMap.keySet().stream().map(key -> handlerMap.get(key).checkEvent(info)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public String getMessage(StockInfoRes info) {
        return handlerMap.get(info.getTypeEvent()).createMessage(info);
    }
}
