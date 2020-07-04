package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.domain.Question;
import ru.otus.spring.helper.Parser;
import ru.otus.spring.service.QuestionService;
import ru.otus.spring.service.QuestionServiceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Тест проверяет: ")
class QuestionDaoImplTest {


    @Configuration
    static class NestedConfiguration {

        @MockBean
        private Parser parser;

        @Bean
        QuestionDao dao() {
            return new QuestionDaoImpl(parser);
        }
    }

    @Autowired
    private Parser parser;

    @Autowired
    private QuestionDao dao;

    @Test
    @DisplayName("корректную работы основных методов дао ")
    void correctWorkOfDao() throws IOException {
        List<Question> list = List.of(new Question(123, "Что происходит?", Collections.emptyList(), 1));
        when(parser.parseQuestions()).thenReturn(list);
        assertDoesNotThrow(() -> {
            assertThat(dao.findAll()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("корректную работы основных методов дао при ошибках парсинга")
    void correctWorkOfDaoWhenParsingRuntimeException() throws IOException {
        doThrow(new IOException()).when(parser).parseQuestions();
        assertThrows(QuestionDaoException.class, dao::findAll);
    }


}