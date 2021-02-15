package ru.otus.spring.sectorservice;

import ru.otus.spring.sectorservice.entity.StockType;

import java.util.List;

/**
 * сервис для работы с репозиторием типов ценных бумаг
 */
public interface SectorDbService {

    /**
     * метод предназначен для получения сектора экономики компании из БД
     *
     * @param ticker - тикер для определения типа
     * @return тип сектора
     */
    String getSectorByTicker(String ticker);

    /**
     * получение всех секторов
     *
     * @return список всех секторов
     */
    List<StockType> getAll();

    /**
     * удаление сектора
     *
     * @param id -id сектора для удаления
     */
    void delete(String id);

    /**
     * добавление сектора
     *
     * @param stockType - объект для сохранения
     */
    void add(StockType stockType);


}
