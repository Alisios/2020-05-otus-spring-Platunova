package ru.otus.spring.configs;

import org.springframework.cloud.stream.annotation.EnableBinding;
import ru.otus.spring.kafka.StockStreams;

@EnableBinding(StockStreams.class)
public class KafkaConfigs {
}
