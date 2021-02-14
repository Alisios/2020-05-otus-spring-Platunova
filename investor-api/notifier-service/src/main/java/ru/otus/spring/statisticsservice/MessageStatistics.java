package ru.otus.spring.statisticsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "mail-stat")
@RequiredArgsConstructor
public class MessageStatistics {

    //private final StatRepository repository;

    //@ReadOperation
    //public StatElement getMailStatistics() {
       // return repository.findById("statistics").get();
   // }
}
