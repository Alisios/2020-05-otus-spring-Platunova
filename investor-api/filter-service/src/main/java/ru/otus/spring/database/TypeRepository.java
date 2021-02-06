package ru.otus.spring.database;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.otus.spring.database.entity.StockType;

import java.util.List;
import java.util.Optional;

public interface TypeRepository extends KeyValueRepository<StockType, String> {

    Optional<StockType> findById(String id);

    Optional<StockType> findByTicker(String ticker);

    List<StockType> findAll();

    void deleteById(String id);
}
