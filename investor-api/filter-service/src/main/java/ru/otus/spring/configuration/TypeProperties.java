package ru.otus.spring.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "type-properties")
@Component("typeProperties")
@Data
@NoArgsConstructor
public class TypeProperties {

    private String bond;
    private String stock;
    private String ETF;
    private String currency;

}
