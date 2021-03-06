package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.User;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("Тест проверяет: ")
@SpringBootTest
class TestServiceImplTest {

    @Configuration
    @ComponentScan("ru.otus.spring.service")
    static class NestedConfiguration { }

    private final User user = new User("Иванов", "Петя", true);

    @MockBean
    private TestServiceProperties testServiceProperties;
    @MockBean
    private IOService ioService;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private ConverterService converterService;
    @MockBean
    private Localizer localizer;

    @Autowired
    private TestHandler testService;

    private final List<Question> list = List.of(
            new Question(123, "Что происходит?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 1));

    @BeforeEach
    void setUp() {
        when(localizer.printFailedResultOfTest()).thenReturn("failed");
        when(localizer.printSuccessResultOfTest()).thenReturn("passed");
        when(testServiceProperties.getRightAnswersMin()).thenReturn(1);
    }

    @Test
    @DisplayName("корректно создает студента")
    void correctlyCreateUser() {
        when(ioService.inputMessage()).thenReturn("Вася").thenReturn("Петров");
        User user = testService.getUserFromInput();
        assertThat(user.getName()).isEqualTo("Вася");
        assertThat(user.getSurname()).isEqualTo("Петров");
        assertThat(user.getIsTested()).isFalse();
    }

    @Test
    @DisplayName("корректное определение правильности ответа студента и вывод о сдаче теста")
    void correctlyDefinesRightAnswerOfStudentAndPassedResults() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(converterService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        when(ioService.inputMessage()).thenReturn("2");
        assertThat(testService.printResultsOfTest(testService.executeTest(user))).contains("1").containsIgnoringCase("passed")
                .contains(String.valueOf(testServiceProperties.getRightAnswersMin()));
    }

    @Test
    @DisplayName("корректное определение правильности ответа студента и вывод о несдаче теста")
    void correctlyDefinesRightAnswerOfStudentAndFailedReesults() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(converterService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        when(ioService.inputMessage()).thenReturn("3");
        assertThat(testService.printResultsOfTest(testService.executeTest(user))).doesNotContain("1");
        assertThat(testService.printResultsOfTest(testService.executeTest(user))).containsIgnoringCase("failed");
    }

    @Test
    @DisplayName("корректную реакцию на неправильный ввод студента")
    void correctlyHandleWrongInputOfStudent() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(converterService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        doThrow(new NumberFormatException()).when(ioService).inputMessage();
        assertDoesNotThrow(() -> {
            assertThat(testService.printResultsOfTest(testService.executeTest(user)))
                    .isNotEqualTo(1);
        });
    }

    @Test
    @DisplayName("бросает исключение при невозможности открыть тест")
    void correctlyThrowsException() throws QuestionDaoException {
        doThrow(new QuestionDaoException("")).when(questionService).getAll();
        assertThrows(QuestionDaoException.class,()-> testService.executeTest(user));
    }
}