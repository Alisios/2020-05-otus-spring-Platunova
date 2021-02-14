package ru.otus.spring.kafka;
import ru.otus.spring.commons.ErrorMessage;

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


}
