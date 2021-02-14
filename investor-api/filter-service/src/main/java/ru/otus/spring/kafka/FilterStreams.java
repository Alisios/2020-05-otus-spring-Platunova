package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface FilterStreams {

    String STOCK_INFO_IN = "stock-info-in";
    String ERROR_OUT = "error-out";
    String SUBSCRIBING_FILTER_OUT = "subscribing-filter-out";
    String ARCHIVE_OUT= "archive-out";

    @Input(STOCK_INFO_IN)
    SubscribableChannel InWithInfo();

    @Output(ERROR_OUT)
    MessageChannel outToError();

    @Output(SUBSCRIBING_FILTER_OUT)
    MessageChannel outToSubscribers();

    @Output(ARCHIVE_OUT)
    MessageChannel outToArchive();
}
