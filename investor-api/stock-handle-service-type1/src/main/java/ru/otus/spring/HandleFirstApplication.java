package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry
@EnableEurekaClient
public class HandleFirstApplication {

    public static void main(String[] args) {

         SpringApplication.run(HandleFirstApplication.class);

    }
}
