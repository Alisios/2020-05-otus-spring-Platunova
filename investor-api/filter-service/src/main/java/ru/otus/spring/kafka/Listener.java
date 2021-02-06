package ru.otus.spring.kafka;

import ru.otus.spring.classifierservice.StockInfoFull;

/**
 * Интерфейс, ожидающий сообщения с информацией
 *
 * @param <T> - тип получаемой информации
 */
public interface Listener<T, R> {

    /**
     * Метод предназначен для получения информации из брокера
     *
     * @param stockInfo - сообщение с нужной информацией
     * @return сообщение обогащенное новой информацией
     */
    R handleMessage(T stockInfo);
}
