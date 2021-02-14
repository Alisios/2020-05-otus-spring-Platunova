package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.otus.spring.handleservice.HandleService;
import ru.otus.spring.handleservice.models.StockInfoFull;

/**
 * Сервис, ожидающий сообщения от кафки и посылающий
 * обработанное сообщение в следующий топик
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StockInfoListener implements Listener<StockInfoFull> {

    HandleService handleService;
    KafkaProducer producer;

    @StreamListener(HandleStreams.SUBSCRIBING_FILTER_IN)
    //@SendTo(HandleStreams.SUBS_OUT)
    public void handleMessage(@Payload StockInfoFull stockInfo) {
        log.info("Получено сообщение из кафки: {}", stockInfo);
        handleService.handleMessageFromExchange(stockInfo).forEach(producer::sendInfo);
    }
}
