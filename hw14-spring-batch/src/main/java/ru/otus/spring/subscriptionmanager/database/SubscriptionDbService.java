package ru.otus.spring.subscriptionmanager.database;

import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDtoFromUser;
import ru.otus.spring.subscriptionmanager.database.entities.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * интерфейс сервиса, осуществоляющий взаимодействие с репозиторием подписок
 */
public interface SubscriptionDbService {

    Subscription saveSubscription(SubscriptionDtoFromUser userDto);

    Optional<SubscriptionDto> getByTickerAndEventType(String ticker, String eventType);

    UUID deleteSubscription(String ticker, String event);

    List<SubscriptionDto> getAllSubscriptions();


    UUID deleteUser(String ticker, String event, String id);

}
