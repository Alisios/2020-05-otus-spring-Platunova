package ru.otus.spring.handleservice.eventservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.cacheservice.CacheService;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.models.CacheStockInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoMapper;
import ru.otus.spring.handleservice.models.StockInfoRes;

@Service("${event-type-handler.eventType.event_3}")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventTypeServiceMin implements EventTypeService {

    StockInfoMapper mapper;
    MessageProperties properties;
    CacheService cacheService;

    @Override
    public StockInfoRes checkEvent(StockInfoFull info) {
        if (cacheService.getCacheBuilder() == null) {
            log.error("Ошибка обращения к кешу при обработке сообщения");
            return null;
        }
        if (info.getLastCost() < ((CacheStockInfo) cacheService
                .getCacheBuilder().
                        get(info.getTicker())).getLow()) {
            StockInfoRes res = mapper.map(info);
            res.setTypeEvent(properties.getMin().get("name"));
            res.setMin(((CacheStockInfo) cacheService.getCacheBuilder().get(info.getTicker())).getLow());
            return res;
        } else return null;
    }

    @Override
    public String createMessage(StockInfoRes info) {
        return properties.getMin().get("message") + " "
                + info.getCompanyName() + " "
                + properties.getMin().get("middle")
                +  " " +info.getMin() + "!";
    }
}
