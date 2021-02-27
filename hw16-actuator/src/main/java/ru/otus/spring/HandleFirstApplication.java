package ru.otus.spring;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableRetry
@EnableMongock
@EnableScheduling
@EnableMongoRepositories
public class HandleFirstApplication {

    public static void main(String[] args) {

         SpringApplication.run(HandleFirstApplication.class);

    }
}
