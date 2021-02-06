package ru.otus.spring.configs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file-properties")
@Component
@Data
@NoArgsConstructor
public class FileProperties {
    
    private String filePath;
    private String inputFile;
}
