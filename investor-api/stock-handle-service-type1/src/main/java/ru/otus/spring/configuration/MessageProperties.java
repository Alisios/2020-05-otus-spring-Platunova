package ru.otus.spring.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@ConfigurationProperties(prefix = "type-properties")
@Component("messageProperties")
@Data
@NoArgsConstructor
public class MessageProperties {

    private double maxChange;
    private long maxCost;
    private long minCost;

    private HashMap<String, String> change;

    private HashMap<String, String> max;

    private HashMap<String, String> min;

    private long cacheLifeTime;

}
