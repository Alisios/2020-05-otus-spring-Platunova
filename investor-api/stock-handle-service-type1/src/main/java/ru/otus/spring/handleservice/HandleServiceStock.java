package ru.otus.spring.handleservice;


import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.otus.spring.handleservice.models.ResultInfo;
import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoRes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис предназначен для обработки сообщения с биржи (тип акция)
 * и формирования сообщения пользователю в зависимости от типа подписки
 */

@Data
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HandleServiceStock implements HandleService {

    MessageCreator messageCreator;
    EventService eventService;


    @Override
    public List<ResultInfo> handleMessageFromExchange(StockInfoFull info) {
        List<StockInfoRes> stockInfo = eventService.getResult(info);
        return stockInfo.stream().map(stock -> ResultInfo.builder()
                .id(UUID.randomUUID().toString())
                .message(messageCreator.createMessage(stock))
                .typeEvent(stock.getTypeEvent())
                .ticker(info.getTicker())
                .min(stock.getMin())
                .max(stock.getMax())
                .change(stock.getChange())
                .lastCost(stock.getLastCost())
                .build())
                .collect(Collectors.toList());
    }
}
