package ru.otus.spring.handleservice;

import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoRes;

import java.util.List;

/**
 * Интерфейс сервиса, обрабатывающего сообщение типа "акция" и определяющий, срабатывает ли триггер
 * на определенные событие
 */
public interface EventService {

    /**
     * метод предназначен для определения наличия срабатывания события.
     * в зависимотсти от проверок определяется тип события.
     * Одно собтие может относитсяи к нескольким типам, тогда формируются несколько сообщений (событий)
     * с разным типом
     *
     * @param info информация с биржи (с наполненной информацией)
     * @return - список событий
     */
    List<StockInfoRes> getResult(StockInfoFull info);
}
