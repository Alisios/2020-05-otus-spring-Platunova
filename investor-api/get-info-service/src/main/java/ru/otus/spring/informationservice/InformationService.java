package ru.otus.spring.informationservice;


/**
 * Интерфейс получения информации с биржи
 *
 * @param <T> тип получаемой информация
 */
public interface InformationService<T> {

    /**
     * метод предназначен для получения информации с биржи
     *
     * @return полученную информацию
     */
    T getStockInfoForKafka();

}
