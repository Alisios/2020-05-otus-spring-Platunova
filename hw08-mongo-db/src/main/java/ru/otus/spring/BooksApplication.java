package ru.otus.spring;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongock
@EnableMongoRepositories
@ImportAutoConfiguration(TransactionAutoConfiguration.class)
public class BooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class);

    }
}
