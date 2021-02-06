package ru.otus.spring.kafka;

import org.springframework.messaging.support.ErrorMessage;
import ru.otus.spring.informationservice.StockInfo;

/**
 * интерфейс отправки сообщений
 */
public interface Producer {

    /**
     * отправка сообщения об ошибки
     *
     * @param error сообщение об ошибке
     */
    void sendError(ErrorMessage error);

    /**
     * отправка сообщения с информацией с биржи
     *
     * @param stockInfo информация с биржи
     */
    void sendInfo(StockInfo stockInfo);

}
