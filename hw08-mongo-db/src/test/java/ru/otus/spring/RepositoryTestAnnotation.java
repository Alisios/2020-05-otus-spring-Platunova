package ru.otus.spring;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ComponentScan({"ru.otus.spring.configs", "ru.otus.spring.repository", "ru.otus.spring.changelog"})
@EnableConfigurationProperties
public @interface RepositoryTestAnnotation {
}
