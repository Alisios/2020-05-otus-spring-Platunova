package ru.otus.spring.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.IOServiceImpl;

import java.util.Scanner;

@Configuration
public class BookServiceConfig {

    @Bean
    IOService outputServiceImpl() {
        return new IOServiceImpl(System.out, new Scanner(System.in));
    }

}
