package ru.otus.spring.stockinfoservice;

import ru.otus.spring.stockinfoservice.database.dto.StockDto;

import java.util.List;


public interface StockManager {

    List<StockDto> getAllStocksOfLastDay();

    void saveAll(List<StockDto> list);

    void clearCurrentTable();
}
