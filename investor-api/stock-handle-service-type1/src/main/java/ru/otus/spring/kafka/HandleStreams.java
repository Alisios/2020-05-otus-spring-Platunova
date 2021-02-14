package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface HandleStreams {

    String ERROR_OUT = "error-out";
    String SUBSCRIBING_FILTER_IN = "subscribing-filter-in";
    String SUBS_OUT = "info-for-subs";
    String UPDATE_CACHE_IN = "update-cache-in";

    @Input(SUBSCRIBING_FILTER_IN)
    SubscribableChannel InWithInfo();

    @Input(UPDATE_CACHE_IN)
    SubscribableChannel InWithInfoForCache();

    @Output(ERROR_OUT)
    MessageChannel outToError();

    @Output(SUBS_OUT)
    MessageChannel outToSubs();
}
