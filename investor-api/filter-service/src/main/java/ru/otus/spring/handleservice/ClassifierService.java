package ru.otus.spring.handleservice;


/**
 * интерфейс классификации ценных бумаг
 */
public interface ClassifierService {

    /**
     * метод предназначен для определения типа ценной бумаги
     *
     * @param stockInfo - пришедшая информация о ценной бумаге
     * @return тип ценной бумаги
     */
    String defineType(StockInfo stockInfo);

    /**
     * метод предназначе для определения сектора экономики,
     * которому принадлежит компания ценной бумаги
     *
     * @param stockInfo- пришедшая информация о ценной бумаге
     * @return - сектор экономики компании
     */
    String defineSector(StockInfo stockInfo);

}
