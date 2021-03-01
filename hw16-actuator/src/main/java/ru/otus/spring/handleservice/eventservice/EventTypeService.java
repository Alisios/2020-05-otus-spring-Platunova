package ru.otus.spring.handleservice.eventservice;

import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoRes;

public interface EventTypeService {

    StockInfoRes checkEvent(StockInfoFull stockInfoFull);

    String createMessage(StockInfoRes info);

    String getType();
}
