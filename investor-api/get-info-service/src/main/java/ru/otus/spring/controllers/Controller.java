package ru.otus.spring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.informationservice.StockInfo;

import java.time.LocalDateTime;

/**
 * для отладки тестовый
 */
@RestController
public class Controller {

    @GetMapping("/info/source/1/")
    public StockInfo getLastInfoByTicker() {
        return StockInfo.builder().companyName("TestName_Bond")
                .high(213.5)
                .close(200.5)
                .low(100.5)
                .date(LocalDateTime.now())
                .ticker("test").build();
    }

    @GetMapping("/info/source/2")
    public StockInfo getLastInfoByTicker2() {
        return StockInfo.builder().companyName("TestName#2")
                .high(253.5)
                .close(200.5)
                .low(100.5)
                .date(LocalDateTime.now())
                .ticker("test").build();
    }
}
