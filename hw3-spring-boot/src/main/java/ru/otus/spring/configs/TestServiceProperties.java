package ru.otus.spring.configs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@ConfigurationProperties(prefix = "service")
@Component("testServiceProperties")
@Setter
@Getter
@NoArgsConstructor
public class TestServiceProperties {

    private int rightAnswersMin;
    private Locale locale;
    private List<String> localeMessagesKeys;
    private String nameOfCsvFileWithQuestionsAndAnswers;
}
