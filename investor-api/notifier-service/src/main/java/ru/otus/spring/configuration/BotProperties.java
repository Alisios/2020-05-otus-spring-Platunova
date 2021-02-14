package ru.otus.spring.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "bot")
@Component
@Data
@NoArgsConstructor
public class BotProperties {

    private String name;
    private String tokien;
    private long reconnectPause;
}
