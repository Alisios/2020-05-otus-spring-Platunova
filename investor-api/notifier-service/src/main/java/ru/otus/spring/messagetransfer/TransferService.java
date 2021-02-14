package ru.otus.spring.messagetransfer;


import ru.otus.spring.kafka.StockInfoForUser;

/**
 * Интерфейс сервиса, который осуществляет отправку уведомления пользователю
 */
public interface TransferService {

    /**
     * отправка сообщения о событии пользователю
     *
     * @param info - уведомление о событии, на которое пользоваетль был подписан
     */
    void sendToUser(StockInfoForUser info);
}
