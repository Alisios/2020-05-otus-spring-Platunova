package ru.otus.spring.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import ru.otus.spring.commons.ErrorMessage;

/**
 * Сервис, реализующий отправку сообщения в кафку
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer implements Producer {

    private final NotifierStreams infoStreams;

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
}
