package ru.otus.spring;


import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
@EnableJpaRepositories(basePackages = "ru.otus.spring.repository")
@EntityScan("ru.otus.spring.domain")
@AutoConfigureTestDatabase
@AutoConfigureDataJpa
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public @interface IntegrationTest {
}
