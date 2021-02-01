package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.spring.stockservice.StockService;
import ru.otus.spring.storage.dto.StockDto;


@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Input {

    StockService stockService;

    @StreamListener(StockStreams.INPUT)
    public void addSubscriber(@Payload StockDto stockDto) {
        try {
            log.info("Сообщение из Kafka: {}", stockDto);
            stockService.addStock(stockDto);
        } catch (Exception ex) {
            log.error("Ошибка при обработке сообщения из кафки", ex);
        }

    }

}
