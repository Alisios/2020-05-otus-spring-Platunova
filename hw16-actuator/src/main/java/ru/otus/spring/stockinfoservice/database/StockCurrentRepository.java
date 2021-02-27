package ru.otus.spring.stockinfoservice.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.stockinfoservice.database.entity.StockEntity;
import ru.otus.spring.stockinfoservice.database.entity.StockEntityCurrent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StockCurrentRepository extends MongoRepository<StockEntityCurrent, String> {

    List<StockEntityCurrent> findAllByTickerAndDateBetweenOrderByDateDesc(String ticker, LocalDateTime before, LocalDateTime after);

    Optional <StockEntityCurrent> findTopByTickerOrderByDateDesc(String ticker);

    List<StockEntityCurrent> findAllByOrderByDateDesc();

    Optional <StockEntityCurrent> findTopByCompanyName(String companyName);

    List<StockEntityCurrent> findAllByType(String type);

    List<StockEntityCurrent> findAllByDateAfterOrderByDate(LocalDateTime localDateTime);

    List<StockEntityCurrent> findTopByOrderByDateDesc();

    Optional<StockEntityCurrent> findById(String id);
}
