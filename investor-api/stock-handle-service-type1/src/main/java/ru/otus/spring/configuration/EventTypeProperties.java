package ru.otus.spring.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "event-type-handler")
@Data
@NoArgsConstructor
public class EventTypeProperties {

    private HashMap<String, String> eventType;
}
