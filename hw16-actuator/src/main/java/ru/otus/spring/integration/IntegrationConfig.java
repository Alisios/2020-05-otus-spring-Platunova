package ru.otus.spring.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import java.time.LocalDateTime;

@Configuration
@IntegrationComponentScan
@Slf4j
public class IntegrationConfig {

    private static final int DEFAULT_QUEUE_CAPACITY = 100;

    private static final String UPDATE_CACHE = "updateInfo";


    @Bean
    public PublishSubscribeChannel cacheChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public QueueChannel cacheInfoChannel() {
        return MessageChannels.queue(DEFAULT_QUEUE_CAPACITY).get();
    }

    @Bean (name = PollerMetadata.DEFAULT_POLLER )
    public PollerMetadata poller () {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get() ;
    }


    @Bean
    public IntegrationFlow cacheFlow() {
        return f -> f.channel(cacheInfoChannel())
//                .handle((m)->{
//                        log.info("Получено сообщение из кафки для обновления кэша: {}", LocalDateTime.now()))
//                })
                .handle("cacheService", UPDATE_CACHE);
    }

}
