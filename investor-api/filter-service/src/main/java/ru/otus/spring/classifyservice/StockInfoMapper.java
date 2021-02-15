package ru.otus.spring.classifyservice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.spring.classifyservice.StockInfo;
import ru.otus.spring.classifyservice.StockInfoFull;

@Mapper(componentModel = "spring")
public interface StockInfoMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "sector", ignore = true)
    StockInfoFull map(StockInfo stockInfo);
}
