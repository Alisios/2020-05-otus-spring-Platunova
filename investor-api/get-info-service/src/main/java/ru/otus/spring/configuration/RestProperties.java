package ru.otus.spring.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "rest")
@Component
@Setter
@Getter
@NoArgsConstructor
public class RestProperties {
    private String host;
    private String scheme;
    private String port;
    private String pathInfoSource1;
    private String pathInfoSource2;
    private String pathInfoSource3;
    private String pathInfoSource4;
}
