package ru.otus.spring.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.Localizer;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuestionServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Тесты проверяют: ")
@SpringBootTest
class ParserCsvTest {

    @Configuration
    static class NestedConfiguration {
        @MockBean
        private Localizer localizer;

        @Bean
        Parser parser() {
            return new ParserCsv(localizer);
        }
    }

    @Autowired
    private Localizer localizer;

    @Autowired
    private Parser parser;


    @Test
    @DisplayName("корректный парсинг и маппинг csv")
    void correctParsingAndMappingOfCsv() throws IOException {
        when(localizer.getLocalizedQuestionFile()).thenReturn("questions-and-answers.csv");
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
        when(localizer.getLocalizedQuestionFile()).thenReturn("questions-and-answersError.csv");
        assertThrows(RuntimeException.class, parser::parseQuestions);
    }

    @Test
    @DisplayName("бросание исключения при отсуствии файла с данным имененм")
    void correctThrowingOfExceptionOfNullFile() {
        when(localizer.getLocalizedQuestionFile()).thenReturn("questions-and-answersError5678.csv");
        assertThrows(Exception.class, () -> {
            parser.parseQuestions();
        });
    }


}