package ru.otus.spring;


import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@SpringBootTest
@DisplayName("Интеграционный тест проверяет:")
@AutoConfigureDataMongo
@EnableMongoRepositories(basePackages = "ru.otus.spring.repository")
@ComponentScan({"ru.otus.spring.configs", "ru.otus.spring.changelog"})
@EntityScan("ru.otus.spring.domain")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ImportAutoConfiguration(TransactionAutoConfiguration.class)
public @interface IntegrationTestAnnotation {
}
