package ru.otus.spring.kafka;

/**
 * Интерфейс, ожидающий сообщения с информацией
 *
 * @param <T> - тип получаемой информации
 */
public interface Listener<T> {

    /**
     * Метод предназначен для получения информации из брокера
     *
     * @param stockInfo - сообщение с нужной информацией
     * @return сообщение обогащенное новой информацией
     */
    void handleMessage(T stockInfo);
}
