package ru.otus.spring.stockinfoservice.database;

import ru.otus.spring.stockinfoservice.database.dto.StockDto;
import ru.otus.spring.stockinfoservice.database.dto.StockDtoCurrent;
import ru.otus.spring.stockinfoservice.database.entity.StockEntityCurrent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Сервис по работе с данными о компаниях (сегодняшних оперативных)
 */

public interface StockCurrentServiceDb {
    /**
     * Добавление новой записи (из кафки из внешнего источника)
     *
     * @param StockDtoCurrent - новая информация
     */
    void addStock(StockDtoCurrent StockDtoCurrent);

    StockEntityCurrent getById(String id);

    void deleteStock(StockDtoCurrent StockDtoCurrent);

    List<StockEntityCurrent> getAllStocks();

    /**
     * Получение информации об определенной компании за указанный промежуток времени
     *
     * @param ticker - уникальное обозначение компании на бирже
     * @param before - с какого момента нудны данные
     * @param after  - до какого момента нужны данные
     * @return список полученных результатов в порядке от самых новых к старым
     */
    List<StockEntityCurrent> getByInterval(String ticker, LocalDateTime before, LocalDateTime after);

    /**
     * Получение самой последней (по дате) информации о комапнии
     *
     * @param ticker -  уникальное обозначение компании на бирже
     * @return информация по компании
     */
    StockEntityCurrent getLastInfoByTicker(String ticker);

    StockEntityCurrent getLastInfoByCompanyName(String companyName);

    /**
     * Получение последеней имеющейся информации (по дате) о всех компаниях из выбранного сектора
     *
     * @param type - сектор, к которому принадлежит компания
     * @return список результатов
     */
    List<StockEntityCurrent> getByType(String type);

    List<StockDto> getAllLastStocks();

    void clear();
}
