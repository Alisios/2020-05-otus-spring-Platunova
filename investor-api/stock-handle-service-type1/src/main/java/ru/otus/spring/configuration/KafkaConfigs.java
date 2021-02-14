package ru.otus.spring.configuration;

import org.springframework.cloud.stream.annotation.EnableBinding;
import ru.otus.spring.kafka.HandleStreams;


@EnableBinding(HandleStreams.class)
public class KafkaConfigs {
}
