package ru.otus.spring.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.spring.checkservice.CheckSubscriptionService;
import ru.otus.spring.checkservice.ResultInfo;
import ru.otus.spring.checkservice.StockInfoForUser;
import ru.otus.spring.common.CrudMessage;
import ru.otus.spring.common.ErrorMessage;
import ru.otus.spring.subscriptionmanager.SubscriptionDbServiceException;
import ru.otus.spring.subscriptionmanager.SubscriptionManager;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KafkaStreamProcessor {

    SubscriptionManager subscriptionManager;
    CheckSubscriptionService checkSubscriptionService;
    KafkaProducer kafkaProducer;


    @StreamListener(SubscribingServiceStreams.SUBSCRIPTION_DB_IN)
    public void crud(@Payload CrudMessage crudMessage) {
        try {
            log.info("Сообщение из Kafka: crud с подписками {}", crudMessage);
            subscriptionManager.handleCrudFromUser(crudMessage);
        } catch (SubscriptionDbServiceException ex) {
            log.error("Ошибка при crud операциях с подписками", ex);
            kafkaProducer.sendErrorMessage(new ErrorMessage(ex, crudMessage != null ? crudMessage.toString() : "", "Ошибка при crud операциях с подписками"));
        }

    }


    @StreamListener(SubscribingServiceStreams.FROM_ADMIN_IN)
    //@SendTo(SubscribingServiceStreams.TO_ADMIN_OUT)
    public void toAdmin() {
        try {
            log.info("Сообщение из Kafka: Получить всех подписчиков");
            kafkaProducer.sendToAdmin(subscriptionManager.getSubsForAdmin());
        } catch (SubscriptionDbServiceException ex) {
            log.error("Ошибка при получении всех подписок", ex);
            kafkaProducer.sendErrorMessage(new ErrorMessage(ex, "", "Ошибка при получении всех подписок"));
        }
    }


    @StreamListener(SubscribingServiceStreams.FROM_EXCHANGE_IN)
    // @SendTo(SubscribingServiceStreams.TO_USER_OUT)
    public void sendToUser(ResultInfo resultInfo) {
        try {
            log.info("Сообщение из Kafka c информацией с биржи");
            List<StockInfoForUser> list = checkSubscriptionService.checkAndSend(resultInfo);
            if (!list.isEmpty())
                list.forEach(kafkaProducer::sendToUser);
        } catch (Exception ex) {
            log.error("Ошибка при обработке сообщения с биржи", ex);
            kafkaProducer.sendErrorMessage(new ErrorMessage(ex, resultInfo != null ? resultInfo.toString() : "", "Ошибка при обработке сообщения с биржи"));
        }
    }
}
