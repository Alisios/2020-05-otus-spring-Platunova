package ru.otus.spring.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тесты проверяют: ")
class ParserCsvTest {

    private final TestServiceProperties testServiceProperties = mock(TestServiceProperties.class);

    @Test
    @DisplayName("корректный парсинг и маппинг csv")
    void correctParsingAndMappingOfCsv() throws IOException {
        when(testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()).thenReturn("questions-and-answers-");
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);

        Parser parser = new ParserCsv(testServiceProperties);
        List<Question> parseQuestions = parser.parseQuestions();
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
        Parser parser = new ParserCsv(testServiceProperties);
        assertThrows(RuntimeException.class, parser::parseQuestions);
    }

    @Test
    @DisplayName("бросание исключения при отсуствии файла с данным имененм")
    void correctThrowingOfExceptionOfNullFile() {
        when(testServiceProperties.getNameOfCsvFileWithQuestionsAndAnswers()).thenReturn("questions-and-answersError123-");
        when(testServiceProperties.getLocale()).thenReturn(Locale.ENGLISH);
        assertThrows(Exception.class, () -> {
            Parser parser = new ParserCsv(testServiceProperties);
            parser.parseQuestions();
        });
    }

}