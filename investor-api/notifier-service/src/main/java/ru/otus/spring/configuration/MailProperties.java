package ru.otus.spring.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mail-properties")
@Component
@Setter
@Getter
@NoArgsConstructor
public class MailProperties {
    private String server_email;

}
