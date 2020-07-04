package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import ru.otus.spring.configs.TestServiceProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Тесты проверяют: ")
class LocalizerImplTest {

    private final TestServiceProperties testServiceProperties = mock(TestServiceProperties.class);
    private final MessageSource messageSource= mock(MessageSource.class);
    private final Localizer localizerImpl = new LocalizerImpl(testServiceProperties, messageSource);

    @BeforeEach
    void setUp(){
        when(testServiceProperties.getLocaleMessagesKeys()).thenReturn(List.of("a"));
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);

    }

    @Test
    @DisplayName("корректное формирование map на основе файла с конфигурациями и локали")
    void correctlyCreateMapAccordingToConfigurationFileAndLocale(){
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("a", "value for a");
        when(messageSource.getMessage("a", null, testServiceProperties.getLocale())).thenReturn(mapForTest.get("a"));
        assertThat(localizerImpl.getLocalizedTestServiceMessages()).isEqualTo(mapForTest);
    }

    @Test
    @DisplayName("корректный выбор файла в зависимости от локали")
    void testServiceProperties2(){
        String nameOfFile = "name";
        when(messageSource.getMessage("csvPath", new String [] {nameOfFile}, testServiceProperties.getLocale())).thenReturn("name-en.csv");
        when(testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()).thenReturn(nameOfFile);
        assertThat(localizerImpl.getLocalizedQuestionFile())
                .doesNotContain("-ru.csv")
                .contains("-en.csv")
                .contains(nameOfFile);
    }

}