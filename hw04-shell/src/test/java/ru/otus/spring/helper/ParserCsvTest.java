package ru.otus.spring.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Тесты проверяют: ")
@SpringBootTest
class ParserCsvTest {

    @Configuration
    @Import(ParserCsv.class)
    static class NestedConfiguration { }

    @MockBean
    private TestServiceProperties testServiceProperties;

    @Autowired
    private Parser parser;

    @Test
    @DisplayName("корректный парсинг и маппинг csv")
    void correctParsingAndMappingOfCsv() throws IOException {
        when(testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()).thenReturn("questions-and-answers-");
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);
        List<Question> parseQuestions = parser.parseQuestions();
        assertThat(parseQuestions.get(0)).hasFieldOrPropertyWithValue("contentOfQuestion","And how do you like this, Elon Musk?");
        assertThat(parseQuestions).isNotEmpty();
        parseQuestions.forEach(answer -> {
            assertThat(answer.getListOfAnswers()).isNotEmpty();
            assertThat(answer.getListOfAnswers().size()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("бросание исключения при некорректной заполненности сsv")
    void correctThrowingOfExceptionOfIncorrectDateInCsv() {
        when(testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()).thenReturn("questions-and-answersError-");
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);
        assertThrows(RuntimeException.class, parser::parseQuestions);
    }

    @Test
    @DisplayName("бросание исключения при отсуствии файла с данным имененм")
    void correctThrowingOfExceptionOfNullFile() {
        when(testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()).thenReturn("questions-and-answersError123-");
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);
        assertThrows(Exception.class, () -> {
            parser.parseQuestions();
        });
    }


}