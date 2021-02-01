//package ru.otus.spring.kafka;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//import ru.otus.spring.commons.Event;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class Producer {
//
//    private final ApplicationEventPublisher publisher;
//
//    public void send(Event event) {
//        log.info("first event in kafka {}", event);
//        publisher.publishEvent(event);
//    }
//
//
//}
