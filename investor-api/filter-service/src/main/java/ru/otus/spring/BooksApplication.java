package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry
public class BooksApplication {

    public static void main(String[] args) {

         SpringApplication.run(BooksApplication.class);

    }
}
