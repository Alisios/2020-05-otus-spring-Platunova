package ru.otus.spring.configuration;

import org.springframework.cloud.stream.annotation.EnableBinding;
import ru.otus.spring.kafka.FilterStreams;

@EnableBinding(FilterStreams.class)
public class KafkaConfigs {
}
