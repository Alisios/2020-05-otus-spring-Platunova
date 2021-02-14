package ru.otus.spring.handleservice;

import ru.otus.spring.handleservice.models.ResultInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;

import java.util.List;

public interface HandleService {

    List<ResultInfo> handleMessageFromExchange(StockInfoFull info);
}
