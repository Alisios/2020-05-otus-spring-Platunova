package ru.otus.spring.storage.dto;

import org.mapstruct.Mapper;
import ru.otus.spring.storage.StockEntity;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface StockMapper {
    DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HHmmss");

    StockEntity map(StockDto stockDto);

    StockDto map(StockEntity stockEntity);

    StockEntity map(StockDtoToUser stockDto);

    StockDtoToUser mapForUser(StockEntity stockEntity);

    //StockDtoToUser mapForUser(StockEntityShort stockEntityShort);


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