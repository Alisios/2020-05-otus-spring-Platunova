package ru.otus.spring.configuration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import ru.otus.spring.kafka.InfoStreams;

@EnableBinding(InfoStreams.class)
public class KafkaConfigs {
}
