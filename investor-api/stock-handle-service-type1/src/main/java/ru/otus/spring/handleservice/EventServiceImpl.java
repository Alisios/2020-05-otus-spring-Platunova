package ru.otus.spring.handleservice;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * бизнес логика (пока простейшая)
 * У информации типа "акция" 3 типа собтия - увеличение цены,
 * увеличение текущей цены по сравнению с максимумом предыдущего дня,
 * уменьшение текущей цены по сравнению с минимумом цены предыдущего дня
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    MessageProperties properties;
    StockInfoMapper mapper;
    CacheService cacheService;

    @Override
    public List<StockInfoRes> getResult(StockInfoFull info) {
        final List<StockInfoRes> list = new ArrayList<>();
        try {
            //событие типа 1 - увеличение цены
            if (info.getChange() >= 0) {
                StockInfoRes res = mapper.map(info);
                res.setTypeEvent(properties.getChange().get("name"));
                list.add(res);
            }
            //событие типа 2 - увеличение максимума цены
            if (info.getLastCost() > ((CacheStockInfo) cacheService
                    .getCacheBuilder().
                            get(info.getTicker())).getHigh()) {
                StockInfoRes res = mapper.map(info);
                res.setTypeEvent(properties.getMax().get("name"));
                res.setMax(((CacheStockInfo) cacheService.getCacheBuilder().get(info.getTicker())).getHigh());
                list.add(res);
            }
            //событие типа 3 - уменьшение минимума цены
            if (info.getLastCost() < ((CacheStockInfo) cacheService
                    .getCacheBuilder().
                            get(info.getTicker())).getLow()) {
                StockInfoRes res = mapper.map(info);
                res.setTypeEvent(properties.getMin().get("name"));
                res.setMin(((CacheStockInfo) cacheService.getCacheBuilder().get(info.getTicker())).getLow());
                list.add(res);
            }
        } catch (Exception ex) {
            log.error("Ошибка при обработке акции на определение типа события");
            return Collections.emptyList();
        }
        return list;
    }
}
