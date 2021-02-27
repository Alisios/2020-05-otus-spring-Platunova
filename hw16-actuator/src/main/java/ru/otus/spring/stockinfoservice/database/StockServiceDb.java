package ru.otus.spring.stockinfoservice.database;

import ru.otus.spring.stockinfoservice.database.dto.StockDto;
import ru.otus.spring.stockinfoservice.database.entity.StockEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис по работе с данными о компаниях (исторических)
 */
public interface StockServiceDb {

    /**
     * Добавление новой записи (из кафки из внешнего источника)
     *
     * @param stockDto - новая информация
     */
    void addStock(StockDto stockDto);

    void addAllStocks(List<StockDto> stockDtos);

    StockEntity getById(String id);

    void deleteStock(StockDto stockDto);

    List<StockEntity> getAllStocks();

    /**
     * Получение информации об определенной компании за указанный промежуток времени
     *
     * @param ticker - уникальное обозначение компании на бирже
     * @param before - с какого момента нудны данные
     * @param after  - до какого момента нужны данные
     * @return список полученных результатов в порядке от самых новых к старым
     */
    List<StockEntity> getByInterval(String ticker, LocalDateTime before, LocalDateTime after);

    /**
     * Получение самой последней (по дате) информации о комапнии
     *
     * @param ticker -  уникальное обозначение компании на бирже
     * @return информация по компании
     */
    StockEntity getLastInfoByTicker(String ticker);

    List<StockEntity> getLastInfoByCompanyName(String companyName);

    /**
     * Получение последеней имеющейся информации (по дате) о всех компаниях из выбранного сектора
     *
     * @param type - сектор, к которому принадлежит компания
     * @return список результатов
     */
    List<StockEntity> getByType(String type);

    List<StockEntity> getAllStocksOfLastDay(LocalDateTime localDateTime);

}
