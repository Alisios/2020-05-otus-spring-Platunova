package ru.otus.spring.stockservice;

import ru.otus.spring.storage.dto.StockDto;
import ru.otus.spring.storage.StockEntity;
import ru.otus.spring.storage.dto.StockDtoToUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис по работе с данными о компаниях
 */
public interface StockService {

    /**
     * Добавление новой записи (из кафки из внешнего источника)
     *
     * @param stockDto - новая информация
     */
    void addStock(StockDto stockDto);

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
    List<StockDtoToUser> getByType(String type);

}
