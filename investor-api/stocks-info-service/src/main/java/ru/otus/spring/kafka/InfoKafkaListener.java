package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.spring.commons.ErrorMessage;
import ru.otus.spring.stockinfoservice.database.StockCurrentServiceDb;
import ru.otus.spring.stockinfoservice.database.dto.StockDtoCurrent;


@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InfoKafkaListener {

    StockCurrentServiceDb stockCurrentServiceDb;
    Producer producer;

    @StreamListener(StockStreams.ARCHIVE_IN)
    public void addSubscriber(@Payload StockDtoCurrent stockDto) {
        try {
            log.info("Сообщение из Kafka: {}", stockDto);
            stockCurrentServiceDb.addStock(stockDto);
        } catch (Exception ex) {
            log.error("Ошибка при обработке сообщения из кафки", ex);
            producer.sendError(new ErrorMessage(ex, stockDto != null ? stockDto.toString() : "", "Ошибка при обработке сообщения из кафки из FilterService"));
        }

    }

}
