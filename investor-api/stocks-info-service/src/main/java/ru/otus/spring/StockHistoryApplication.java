package ru.otus.spring;

//import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
//@EnableMongock
@EnableMongoRepositories
@EnableScheduling
//@ImportAutoConfiguration(TransactionAutoConfiguration.class)
public class StockHistoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockHistoryApplication.class);
    }
}
