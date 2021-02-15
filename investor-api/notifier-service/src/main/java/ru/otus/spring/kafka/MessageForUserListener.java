package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.spring.commons.ErrorMessage;
import ru.otus.spring.messagetransfer.NotifyService;
import ru.otus.spring.messagetransfer.TransferException;

/**
 * Сервис, ожидающий события из кафки для отправки
 */
@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageForUserListener {

    KafkaProducer kafkaProducer;

    NotifyService notifyService;

    @StreamListener(NotifierStreams.MESSAGE_FOR_USER_IN)
    public void notify(@Payload StockInfoForUser info) {
        try {
            log.info("Сообщение из Kafka для посылки подписчикам {}", info);
            notifyService.send(info);
        } catch (TransferException ex) {
            log.error("Ошибка при посылке сообщения пользователю!", ex);
            kafkaProducer.sendError(new ErrorMessage(ex, info != null ? info.toString() : "", "Ошибка при посылке сообщения пользователю"));
        }

    }
}
