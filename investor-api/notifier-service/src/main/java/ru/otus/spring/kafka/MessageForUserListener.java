package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.spring.commons.ErrorMessage;
import ru.otus.spring.messagetransfer.TransferException;
import ru.otus.spring.messagetransfer.TransferService;

/**
 * Сервис, ожидающий события из кафки для отправки
 */
@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageForUserListener {

    KafkaProducer kafkaProducer;

    @Qualifier("email_service")
    TransferService transferServiceEmail;

    @Qualifier("telegram-service")
    TransferService transferServiceTm;

    @Autowired
    MessageForUserListener(KafkaProducer kafkaProducer,
                           @Qualifier("email_service") TransferService transferService,
                           @Qualifier("telegram-service") TransferService transferServiceTm) {
        this.kafkaProducer = kafkaProducer;
        this.transferServiceEmail = transferService;
        this.transferServiceTm = transferServiceTm;
    }


    @StreamListener(NotifierStreams.MESSAGE_FOR_USER_IN)
    public void notify(@Payload StockInfoForUser info) {
        try {
            log.info("Сообщение из Kafka для посылки подписчикам {}", info);
            if (info.getEmail() != null)
                transferServiceEmail.sendToUser(info);
            if (info.getTelegram() != null)
                transferServiceTm.sendToUser(info);

        } catch (TransferException ex) {
            log.error("Ошибка при посылке сообщения пользователю!", ex);
            kafkaProducer.sendError(new ErrorMessage(ex, info != null ? info.toString() : "", "Ошибка при посылке сообщения пользователю"));
        }

    }
}
