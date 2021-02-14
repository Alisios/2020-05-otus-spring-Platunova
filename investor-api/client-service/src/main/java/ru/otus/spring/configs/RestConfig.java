package ru.otus.spring.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
@EnableRetry
@EnableHystrix
@EnableHystrixDashboard
@EnableScheduling
@EnableEurekaClient
@RequiredArgsConstructor
public class RestConfig {

    private final RestProperties restProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(restProperties.getConnectTimeout()))
                .setReadTimeout(Duration.ofSeconds(restProperties.getReadTimeout()))
                .build();
    }
}
