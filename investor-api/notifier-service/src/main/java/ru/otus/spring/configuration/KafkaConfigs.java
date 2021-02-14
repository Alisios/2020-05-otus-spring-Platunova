package ru.otus.spring.configuration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import ru.otus.spring.kafka.NotifierStreams;

@EnableBinding(NotifierStreams.class)
public class KafkaConfigs {
}
