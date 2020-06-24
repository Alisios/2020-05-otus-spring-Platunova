package ru.otus.spring.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.service.IOService;
import ru.otus.spring.service.IOServiceImpl;

import java.util.Scanner;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceConfig {

    @Bean
    IOService outputServiceImpl(){
        return new IOServiceImpl(System.out, new Scanner(System.in));
    }
}
