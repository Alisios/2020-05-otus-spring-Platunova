package ru.otus.spring.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface SubscribingServiceStreams {

    String SUBSCRIPTION_DB_IN = "subscriber-db-in";
    String FROM_ADMIN_IN = "from-admin-in";
    String FROM_EXCHANGE_IN = "from-exchange-in";

    String TO_USER_OUT = "message-to-user-out";
    String TO_ADMIN_OUT = "message-to-admin-out";
    String ERROR_OUT = "error-message-out";

    @Input(SUBSCRIPTION_DB_IN)
    SubscribableChannel inboundAddUser();

    @Input(FROM_ADMIN_IN)
    SubscribableChannel inboundFromAdmin();

    @Input(FROM_EXCHANGE_IN)
    SubscribableChannel inboundFromExchange();

    @Output(TO_USER_OUT)
    MessageChannel outToUser();

    @Output(TO_ADMIN_OUT)
    MessageChannel outToAdmin();

    @Output(ERROR_OUT)
    MessageChannel outError();

}
