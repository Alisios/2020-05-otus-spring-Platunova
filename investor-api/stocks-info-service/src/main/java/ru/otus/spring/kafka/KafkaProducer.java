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
import ru.otus.spring.commons.ErrorMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer implements Producer {

    private final StockStreams stockStreams;

    @Override
    public void sendError(ErrorMessage error) {
        log.info("Ошибка обработки в сервисе-хранилище исторчиеских данных ");
        MessageChannel messageChannel = stockStreams.outToError();
        messageChannel.send(MessageBuilder
                .withPayload(error)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}