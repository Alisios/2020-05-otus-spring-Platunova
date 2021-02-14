package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface InfoStreams {

    String STOCK_INFO_OUT = "stock-info-out";
    String ERROR_OUT = "error-out";

    @Output(STOCK_INFO_OUT)
    MessageChannel outWithInfo();

    @Output(ERROR_OUT)
    MessageChannel outToError();
}
