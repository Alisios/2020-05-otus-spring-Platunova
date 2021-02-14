package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StockStreams {

    String ARCHIVE_IN = "archive-in";
    String ERROR_OUT = "error-out";
    String INFO_FOR_CACHE = "update-cache-out";

    @Input(ARCHIVE_IN)
    SubscribableChannel inboundStock();

    @Output(ERROR_OUT)
    MessageChannel outToError();

    @Output(INFO_FOR_CACHE)
    MessageChannel outToHandle();

}
