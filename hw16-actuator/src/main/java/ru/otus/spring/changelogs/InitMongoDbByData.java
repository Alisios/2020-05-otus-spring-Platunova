package ru.otus.spring.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.otus.spring.stockinfoservice.database.StockCurrentRepository;
import ru.otus.spring.stockinfoservice.database.entity.StockEntityCurrent;

import java.time.LocalDateTime;

@ChangeLog(order = "003")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitMongoDbByData {

    @ChangeSet(order = "003", id = "initCurrentStocks", author = "alice", runAlways = true)
    public void initAuthors(StockCurrentRepository repository) {
        repository.save(StockEntityCurrent.builder()
                .date(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .companyName("Газпром")
                .close(212.83)
                .low(210.22)
                .high(231.44)
                .open(212.710)
                .ticker("GAZP")
                .type("Нефть и газ")
                .volume(1261779620)
                .build());
    }

}
