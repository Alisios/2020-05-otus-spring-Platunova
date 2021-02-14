//package ru.otus.spring.storage;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Sort;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class StockRepositoryTest {
//
//    @Test
//    void findByDateBetween() {
//        Sort sort  = Sort.by(Sort.Direction.ASC, "id");
//        LocalDateTime now = LocalDateTime.now();
//        em.persist(...);
//        now.minusHours(1);
//    }
//
//    @Test
//    void findByDateIsNear() {
//    }
//}