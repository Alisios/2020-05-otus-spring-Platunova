package ru.otus.spring.subscriptionmanager;

import ru.otus.spring.common.CrudMessage;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;

import java.util.List;
import java.util.Optional;

/**
 * интерфейс сервиса осуществляющего обработку событий из кафки
 */
public interface SubscriptionManager {

    /**
     * обработка crud операций
     * @param crudMessage
     */
    void handleCrudFromUser(CrudMessage crudMessage);

    /**
     * запрос всех подписчиков для админа
     * @return список всех подписчиков
     */
    List<SubscriptionDto> getSubsForAdmin();

    /**
     * получение подписки определнного типа и компании
     * @param ticker - уникальный идентификатор компании на бирже
     * @param eventType - тип события (тип подписки)
     * @return  подписка на компанию и событие из БД
     */
    Optional<SubscriptionDto> getByTickerAndEventType(String ticker, String eventType);

}
