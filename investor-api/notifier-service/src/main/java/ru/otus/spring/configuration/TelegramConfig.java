package ru.otus.spring.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.otus.spring.telegramservice.Bot;

@Configuration
@RequiredArgsConstructor
@EnableRetry
@EnableEurekaClient
@EnableScheduling
@EnableMapRepositories
public class TelegramConfig {

    private final BotProperties properties;

    @Bean
    Bot ticketBot() {
        ApiContextInitializer.init();
        Bot invest_bot = new Bot(properties);
        invest_bot.botConnect();
        return invest_bot;
    }
}