package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.otus.spring.cacheservice.CacheService;
import ru.otus.spring.handleservice.models.CacheStockInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис, ожидающий сообщения от кафки от архива ежедневно для обновления кеша
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CacheListener {

    CacheService cacheService;

    @StreamListener(HandleStreams.UPDATE_CACHE_IN)
    public void handleMessage(@Payload List<CacheStockInfo> stockInfo) {
        log.info("Получено сообщение из кафки для обновления кэша: {}", LocalDateTime.now());
        cacheService.updateInfo(stockInfo);
    }
}
