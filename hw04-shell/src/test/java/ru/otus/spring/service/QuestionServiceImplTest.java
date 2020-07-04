package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Тест проверяет: ")
@SpringBootTest
class QuestionServiceImplTest {

    @Configuration
    static class NestedConfiguration {
        @MockBean
        private QuestionDao dao;

        @Bean
        QuestionService questionService() {
            return new QuestionServiceImpl(dao);
        }
    }

    @Autowired
    private QuestionDao dao;

    @Autowired
    private QuestionService questionService;

    private final List<Question> list = List.of(
            new Question(123, "Что происходит?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 1),
            new Question(124, "А сейчас?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 2));

    @Test
    @DisplayName("корректное возвращение всех вопросов и ответов")
    void correctlyShowsQuestionsAndAnswers() throws QuestionDaoException {
        when(dao.findAll()).thenReturn(list);
        assertThat(questionService.getAll()).isEqualTo(list);
    }

    @Test
    @DisplayName("корректное отображение всех ответов")
    void correctlyShowsAnswersO() {
        assertThat(questionService.getRightAnswers(list)).isEqualTo(List.of(1,2));
    }


    @Test
    @DisplayName("бросает исключение при невозможности открыть тест")
    void correctlyThrowsException() throws QuestionDaoException {
        doThrow(new QuestionDaoException("")).when(dao).findAll();
        assertThrows(QuestionDaoException.class, questionService::getAll);
    }

}