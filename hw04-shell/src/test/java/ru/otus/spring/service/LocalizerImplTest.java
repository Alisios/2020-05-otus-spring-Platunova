package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.configs.TestServiceProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DisplayName("Тесты проверяют: ")
@SpringBootTest
class LocalizerImplTest {

    @Configuration
    @Import({LocalizerImpl.class, MessageSourceAutoConfiguration.class})
    static class NestedConfiguration {}

    @MockBean
    private TestServiceProperties testServiceProperties;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private Localizer localizer;

    @BeforeEach
    void setUp() {
        when(testServiceProperties.getLocaleMessagesKeys()).thenReturn(List.of("a"));
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test
    @DisplayName("корректное формирование map на основе файла с конфигурациями и локали")
    void correctlyCreateMapAccordingToConfigurationFileAndLocale() {
        Map<String, String> mapForTest = new HashMap<>();
        mapForTest.put("a", "value for a");
        when(messageSource.getMessage("a", null, testServiceProperties.getLocale())).thenReturn(mapForTest.get("a"));
        assertThat(localizer.getLocalizedTestServiceMessages()).isEqualTo(mapForTest);
    }

}