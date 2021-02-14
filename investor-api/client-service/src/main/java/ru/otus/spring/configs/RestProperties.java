package ru.otus.spring.configs;

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
    private String port1;
    private String port2;
    private String userInfoServicePath;
    private String subsServicePath;
    private String subsUserServicePath;
    private String subsServiceAdminPath;

    private long connectTimeout;
    private long readTimeout;
}
