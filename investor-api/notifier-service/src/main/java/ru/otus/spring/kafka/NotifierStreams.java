package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface NotifierStreams {

    String MESSAGE_FOR_USER_IN = "message-to-user-in";
    String ERROR_OUT = "error-out";

    @Input(MESSAGE_FOR_USER_IN)
    SubscribableChannel outWithInfo();

    @Output(ERROR_OUT)
    MessageChannel outToError();
}
