package ru.otus.spring.storage;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends MongoRepository<StockEntity, String> {

    List<StockEntity> findAllByTickerAndDateBetweenOrderByDateDesc(String ticker, LocalDateTime before, LocalDateTime after);

    List<StockEntity> findAllByTickerOrderByDateDesc(String ticker);

    List<StockEntity> findAllByCompanyName(String companyName);

    List<StockEntity> findAllByType(String type);

    Optional<StockEntity> findById(String id);
}
