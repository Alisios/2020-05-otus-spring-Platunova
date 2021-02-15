package ru.otus.spring.typeservice;


import ru.otus.spring.classifyservice.StockInfo;

/**
 * Интерфейс сервиса, определяющего тип ценной бумаги
 */
public interface TypeService {

    String classify(StockInfo stockInfo);
}
