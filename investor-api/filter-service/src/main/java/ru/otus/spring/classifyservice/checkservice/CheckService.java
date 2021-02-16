package ru.otus.spring.classifyservice.checkservice;

import ru.otus.spring.classifyservice.StockInfo;

public interface CheckService {

    boolean checkType(StockInfo stockInfo);

    String getType();
}
