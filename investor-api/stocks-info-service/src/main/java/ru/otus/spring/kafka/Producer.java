package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import ru.otus.spring.commons.ErrorMessage;
import ru.otus.spring.stockinfoservice.database.dto.CacheStockInfo;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Producer {

    StockStreams stockStreams;

    public void sendError(ErrorMessage error) {
        log.info("Отправка в кафку ошибки");
        MessageChannel messageChannel = stockStreams.outToError();
        messageChannel.send(MessageBuilder
                .withPayload(error)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public void sendForUpdate(List<CacheStockInfo> info) {
        log.info("Отправка в кафку информаии для одновления кеша обработчиков ");
        MessageChannel messageChannel = stockStreams.outToHandle();
        messageChannel.send(MessageBuilder
                .withPayload(info)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

}