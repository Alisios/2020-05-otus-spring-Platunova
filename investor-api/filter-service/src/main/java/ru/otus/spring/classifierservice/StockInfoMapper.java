package ru.otus.spring.classifierservice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockInfoMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "sector", ignore = true)
    StockInfoFull map(StockInfo stockInfo);
}
