package ru.otus.spring.checkservice;

import java.util.List;

/**
 * интерфейс сервиса, проверяющего наличие подписки на событие и
 * формирующег особытие для пользователя
 */
public interface CheckSubscriptionService {

    /**
     * Метод предназначен для проверки наличия подписки на событие и в
     * случае наличия формирования сообщения-уведомления пользователю
     *
     * @param info - информация о ценной бумаге
     * @return - список
     */
    List<StockInfoForUser> checkAndSend(ResultInfo info);
}
