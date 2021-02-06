package ru.otus.spring.dispatcher;

import ru.otus.spring.informationservice.StockInfo;

/**
 * сервис периодически принимающий информацию с биржи
 */

public interface InfoDispatcher {

    /**
     * метод предназначен для получения информации из внешнего источника
     * @return информация с биржи
     */
    StockInfo getInfo();

}
