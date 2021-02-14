package ru.otus.spring.statisticsservice;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CleaningService {

    StatRepository repository;


    @Value("${mail-properties.key}")
    private static String KEY;

    @Scheduled(cron = "0 0 23 * * ?")
    void cleaning() {
        repository.deleteAll();
        repository.save(StatElement.builder().id(KEY).dateTime(LocalDateTime.now()).build());
    }

}
