package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.configs.TestServiceProperties;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalizerImpl implements Localizer {

    private final TestServiceProperties testServiceProperties;
    private final MessageSource messageSource;

    @Override
    public Map<String, String> getLocalizedTestServiceMessages() {
        return testServiceProperties.getLocaleMessagesKeys().stream().collect(Collectors.toMap((key) -> key, (key) -> messageSource.getMessage(key, null, testServiceProperties.getLocale())));
    }

    @Override
    public String getLocalizedQuestionFile() {
        return messageSource.getMessage("csvPath", new String[]{testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()}, testServiceProperties.getLocale());
    }

}
