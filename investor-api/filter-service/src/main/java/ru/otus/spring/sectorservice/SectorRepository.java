package ru.otus.spring.sectorservice;


import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.otus.spring.sectorservice.entity.StockType;

import java.util.List;
import java.util.Optional;

public interface SectorRepository extends KeyValueRepository<StockType, String> {

    Optional<StockType> findById(String id);

    Optional<StockType> findByTicker(String ticker);

    List<StockType> findAll();

    void deleteById(String id);
}
