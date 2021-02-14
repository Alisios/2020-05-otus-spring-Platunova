package ru.otus.spring.kafka;


import ru.otus.spring.common.ErrorMessage;

/**
 * интерфейс отправки сообщений
 */
public interface Producer <T> {

    /**
     * отправка сообщения об ошибки
     *
     * @param error сообщение об ошибке
     */
    void sendError(ErrorMessage error);

    /**
     * отправка сообщения с информацией с биржи
     *
     * @param stockInfoFull информация с биржи, обогащенная информацией типа и сектора
     */
    void sendInfo(T stockInfoFull);

}
