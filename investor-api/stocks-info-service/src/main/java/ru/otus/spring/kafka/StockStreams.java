package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StockStreams {

    String INPUT = "archive-in";
    String OUTPUT = "error-out";

    @Input(INPUT)
    SubscribableChannel inboundStock();

    @Output(OUTPUT)
    MessageChannel outToError();

}
