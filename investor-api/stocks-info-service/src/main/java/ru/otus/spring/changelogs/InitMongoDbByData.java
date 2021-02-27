//package ru.otus.spring.changelogs;
//
//import com.github.cloudyrock.mongock.ChangeLog;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//
//@ChangeLog(order = "001")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class InitMongoDbByData {
//
////    @ChangeSet(order = "001", id = "dropDB", author = "alice", runAlways = true)
////    public void dropDB(MongoDatabase database) {
////        database.drop();
////    }
//
////    @ChangeSet(order = "002", id = "initStocks", author = "alice", runAlways = true)
////    public void initAuthors(StockRepository repository) {
////        repository.save(StockEntity.builder()
////                .date(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
////                .companyName("Газпром")
////                .close(212.83)
////                .low(210.22)
////                .high(231.44)
////                .open(212.710)
////                .ticker("GAZP")
////                .type("Нефть и газ")
////                .volume(1261779620)
////                .build());
////    }
//
//}
