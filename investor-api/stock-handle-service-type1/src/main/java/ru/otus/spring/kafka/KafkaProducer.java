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
import ru.otus.spring.handleservice.models.ResultInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.common.ErrorMessage;
import ru.otus.spring.handleservice.models.StockInfoRes;

/**
 * Сервис, реализующий отправку сообщения в кафку
 */
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducer implements Producer<ResultInfo> {

    HandleStreams infoStreams;

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
    public void sendInfo(ResultInfo info) {
        try {
            log.info("Отправка в кафку сообщения {}", info);
            MessageChannel messageChannel = infoStreams.outToSubs();
            messageChannel.send(MessageBuilder
                    .withPayload(info)
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build());
        } catch (Exception e) {
            log.error("Ошибка отправки сообщения c ошибкой в кафку");
        }
    }

}
