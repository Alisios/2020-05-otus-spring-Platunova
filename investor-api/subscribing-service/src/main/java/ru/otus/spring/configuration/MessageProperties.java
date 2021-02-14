package ru.otus.spring.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "message-properties")
@Component
@Data
@NoArgsConstructor
public class MessageProperties {

    Map<String, String> message;

    Map<String, String> eventType;

}
