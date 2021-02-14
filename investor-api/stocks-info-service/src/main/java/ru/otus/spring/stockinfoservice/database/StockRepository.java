package ru.otus.spring.stockinfoservice.database;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.stockinfoservice.database.entity.StockEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends MongoRepository<StockEntity, String> {

    List<StockEntity> findAllByTickerAndDateBetweenOrderByDateDesc(String ticker, LocalDateTime before, LocalDateTime after);

    List<StockEntity> findAllByTickerOrderByDateDesc(String ticker);

    List<StockEntity> findAllByCompanyName(String companyName);

    List<StockEntity> findAllByType(String type);

    List<StockEntity> findAllByDateAfterOrderByDate(LocalDateTime localDateTime);

    Optional<StockEntity> findById(String id);
}
