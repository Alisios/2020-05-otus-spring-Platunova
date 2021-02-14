package ru.otus.spring.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import ru.otus.spring.checkservice.StockInfoForUser;
import ru.otus.spring.common.ErrorMessage;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final SubscribingServiceStreams subscribingServiceStreams;

    public void sendToUser(StockInfoForUser info) {
        log.info("Отправка в кафку сообщения для отсылки пользователю {}", info);
        MessageChannel messageChannel = subscribingServiceStreams.outToUser();
        messageChannel.send(MessageBuilder
                .withPayload(info)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public void sendToAdmin(List <SubscriptionDto> subs) {
        log.info("Отправка в кафку сообщения для отсылки пользователю ");
        MessageChannel messageChannel = subscribingServiceStreams.outToAdmin();
        messageChannel.send(MessageBuilder
                .withPayload(subs)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public void sendErrorMessage(ErrorMessage message) {
        log.info("Отправка в кафку сообщения об ошибке : {}", message);
        MessageChannel messageChannel = subscribingServiceStreams.outError();
        messageChannel.send(MessageBuilder
                .withPayload(message)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

}
