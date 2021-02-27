package ru.otus.spring.stockinfoservice.database.dto;

import org.mapstruct.Mapper;
import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.stockinfoservice.database.entity.StockEntity;
import ru.otus.spring.stockinfoservice.database.entity.StockEntityCurrent;


import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StockMapper {
    DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HHmmss");

    StockEntity map(StockDto stockDto);


    StockEntityCurrent map(StockDtoCurrent stockDto);

    StockDtoCurrent map(StockEntityCurrent stockEntity);

    StockEntity map(StockDtoToUser stockDto);

    StockDtoToUser mapForUser(StockEntity stockEntity);

    StockInfoFull toStockInfoFull(StockEntity stockEntity);

    default StockDto fromCurrentEntityToStockDto(StockEntityCurrent info) {
        return StockDto.builder()
                .close(info.getClose())
                .date(info.getDate())
                .high(info.getHigh())
                .low(info.getLow())
                .open(info.getOpen())
                .ticker(info.getTicker())
                .sector(info.getSector())
                .bondCreditRating(info.getBondCreditRating())
                .companyName(info.getCompanyName())
                .exchange(info.getExchange())
                .id(info.getId())
                .type(info.getType())
                .volume(info.getVolume())
                .adjClose(info.getAdjClose())
                .build();

    }

    default List<CacheStockInfo> toCacheList(List<StockDto> infoList) {
        return infoList.stream().map(info -> CacheStockInfo.builder()
                .close(info.getClose())
                .date(info.getDate())
                .high(info.getHigh())
                .low(info.getLow())
                .open(info.getOpen())
                .ticker(info.getTicker())
                .build()
        ).collect(Collectors.toList());
    }

    default StockEntity mapFromCsv(StockDtoCsv stockDtoCsv, String name) {
        LocalDate date = LocalDate.parse(stockDtoCsv.getDate(), formatter_date);
        LocalTime time = LocalTime.parse(stockDtoCsv.getTime(), formatter_time);
        String companyName = new File(name).getName();
        return StockEntity.builder()
                .companyName(companyName.substring(0, companyName.lastIndexOf('_')))
                .type(companyName.substring(companyName.lastIndexOf('_') + 1, companyName.lastIndexOf('.')))
                .volume(stockDtoCsv.getVolume())
                .ticker(stockDtoCsv.getTicker())
                .open(stockDtoCsv.getOpen())
                .close(stockDtoCsv.getClose())
                .high(stockDtoCsv.getHigh())
                .low(stockDtoCsv.getLow())
                .date(LocalDateTime.of(date.getYear(),
                        date.getMonth(),
                        date.getDayOfMonth(),
                        time.getHour(),
                        time.getMinute(),
                        time.getSecond()))
                .build();
    }

}