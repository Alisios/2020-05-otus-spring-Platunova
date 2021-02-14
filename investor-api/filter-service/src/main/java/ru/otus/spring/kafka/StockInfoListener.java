package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.otus.spring.handleservice.ClassifierService;
import ru.otus.spring.handleservice.StockInfo;
import ru.otus.spring.handleservice.StockInfoFull;
import ru.otus.spring.handleservice.StockInfoMapper;
import ru.otus.spring.configuration.TypeProperties;

/**
 * Сервис, ожидающий сообщения от кафки и посылающий
 * обработанное сообщение в следующий топик
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StockInfoListener implements Listener<StockInfo, StockInfoFull> {

    StockInfoMapper mapper;

    ClassifierService service;

    Producer<StockInfoFull> producer;

    TypeProperties typeProperties;

    @StreamListener(FilterStreams.STOCK_INFO_IN)
    @SendTo(FilterStreams.ARCHIVE_OUT)
    @Override
    public StockInfoFull handleMessage(@Payload StockInfo stockInfo) {
        log.info("Получено сообщение из кафки: {}", stockInfo);
        StockInfoFull stockInfoFull = mapper.map(stockInfo);
        stockInfoFull.setType(service.defineType(stockInfo));
        stockInfoFull.setSector(service.defineSector(stockInfo));
        if (!stockInfoFull.getType().equals(typeProperties.getBond()))
            producer.sendInfo(stockInfoFull);
        return stockInfoFull;
    }
}
