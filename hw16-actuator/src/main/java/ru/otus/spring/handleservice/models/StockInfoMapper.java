package ru.otus.spring.handleservice.models;

import org.mapstruct.Mapper;
import ru.otus.spring.stockinfoservice.database.dto.CacheStockInfo;

@Mapper(componentModel = "spring")
public interface StockInfoMapper {

    StockInfoRes map(StockInfoFull stockInfo);

    default CacheStockInfo mapToCache(StockInfoFull stockInfo) {
        return CacheStockInfo.builder()
                .close(stockInfo.getClose())
                .date(stockInfo.getDate())
                .high(stockInfo.getHigh())
                .low(stockInfo.getLow())
                .ticker(stockInfo.getTicker())
                .open(stockInfo.getOpen())
                .build();
    }
}
