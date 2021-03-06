package ru.otus.spring.handleservice.eventservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.cacheservice.CacheService;
import ru.otus.spring.configuration.EventTypeProperties;
import ru.otus.spring.configuration.MessageProperties;

import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoMapper;
import ru.otus.spring.handleservice.models.StockInfoRes;
import ru.otus.spring.stockinfoservice.database.dto.CacheStockInfo;

@Service("${event-type-handler.eventType.event_2}")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventTypeServiceMax implements EventTypeService {

    StockInfoMapper mapper;
    MessageProperties properties;
    EventTypeProperties eventTypeProperties;
    CacheService cacheService;

    @Override
    public StockInfoRes checkEvent(StockInfoFull info) {
        if (cacheService.getCache() == null) {
            log.error("Ошибка обращения к кешу при обработке сообщения");
            return null;
        }
        if (info.getLastCost() > ((CacheStockInfo) cacheService
                .getCache().
                        get(info.getTicker())).getHigh()) {
            StockInfoRes res = mapper.map(info);
            res.setTypeEvent(properties.getMax().get("name"));
            res.setMax(((CacheStockInfo) cacheService.getCache().get(info.getTicker())).getHigh());
            return res;
        } else return null;
    }

    @Override
    public String createMessage(StockInfoRes info) {
        return properties.getMax().get("message") + " "
                + info.getCompanyName() + " "
                + properties.getMax().get("middle")
                + " " + info.getMax() + "!";
    }

    @Override
    public String getType() {
        return eventTypeProperties.getEventType().get("event_2");
    }
}
