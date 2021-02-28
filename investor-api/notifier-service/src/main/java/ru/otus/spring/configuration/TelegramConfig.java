package ru.otus.spring.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.otus.spring.statisticsservice.StatElement;
import ru.otus.spring.statisticsservice.StatRepository;
import ru.otus.spring.telegramservice.Bot;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@EnableRetry
@EnableEurekaClient
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramConfig {

    BotProperties properties;

    StatRepository statRepository;

    @Bean
    Bot ticketBot() {
        ApiContextInitializer.init();
        Bot invest_bot = new Bot(properties);
        invest_bot.botConnect();
        return invest_bot;
    }

    @Bean
    void initiateDb() {
        statRepository.save(StatElement.builder().id("statistics").mailCount(0).dateTime(LocalDateTime.now()).telegramCount(0).build());
    }
}