package ru.otus.spring.stockinfoservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.commons.exceptions.StockManagerException;
import ru.otus.spring.stockinfoservice.database.StockCurrentServiceDb;
import ru.otus.spring.stockinfoservice.database.StockServiceDb;
import ru.otus.spring.stockinfoservice.database.dto.StockDto;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StockManagerImpl implements StockManager {

    StockCurrentServiceDb stockCurrentServiceDb;
    StockServiceDb stockServiceDb;

    @Override
    public List<StockDto> getAllStocksOfLastDay() {
        try {
            return stockCurrentServiceDb.getAllLastStocks();
        } catch (Exception e) {
             throw new StockManagerException("Ошибка при получении послдених данных об акциях " + LocalDateTime.now(), e);
        }
    }

    @Override
    public void saveAll(List<StockDto> list) {
        try {
            stockServiceDb.addAllStocks(list);
        } catch (Exception e) {
             throw new StockManagerException("Ошибка при сохранении всех акций " + LocalDateTime.now(), e);
        }
    }

    @Override
    public void clearCurrentTable() {
        try {
            stockCurrentServiceDb.clear();
        } catch (Exception e) {
             throw new StockManagerException("Ошибка при очистке таблицы с текущими значениями акций " + LocalDateTime.now(), e);
        }
    }
}
