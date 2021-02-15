package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import ru.otus.spring.classifyservice.StockInfoFull;
import ru.otus.spring.common.ErrorMessage;

/**
 * Сервис, реализующий отправку сообщения в кафку
 */
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducer implements Producer<StockInfoFull> {

    FilterStreams infoStreams;

    @Override
    public void sendError(ErrorMessage error) {
        try {
            log.info("Отправка в кафку ошибки ");
            MessageChannel messageChannel = infoStreams.outToError();
            messageChannel.send(MessageBuilder
                    .withPayload(error)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        } catch (Exception e) {
            log.error("Ошибка отправки сообщения c ошибкой в кафку");
        }
    }

    @Override
    public void sendInfo(StockInfoFull stockInfoFull) {
        try {
            log.info("Отправка сообщения в кафку (акции)");
            MessageChannel messageChannel = infoStreams.outToSubscribers();
            messageChannel.send(MessageBuilder
                    .withPayload(stockInfoFull)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        } catch (Exception e) {
            log.error("Ошибка отправки информационного сообщения в кафку");
        }
    }
}
