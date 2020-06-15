package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Тест проверяет: ")
class TestServiceImplTest {

    private final static int RIGHT = 3;
    private final IOService ioService = mock(IOService.class);
    private final QuestionService questionService = mock(QuestionService.class);
    private final TestService testService = new TestServiceImpl(ioService, questionService, RIGHT);
    private final List<Question> list = List.of(
            new Question(123, "Что происходит?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 1));

    @Test
    @DisplayName("корректное определение привильных ответов студента")
    void correctlyDefinesRightAnswerOfStudent() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(questionService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        when(ioService.inputAnswer()).thenReturn("2");
        assertThat(testService.testStudent()).isEqualTo(List.of(1));
        when(ioService.inputAnswer()).thenReturn("3");
        assertThat(testService.testStudent()).isEqualTo(List.of(0));
    }

    @Test
    @DisplayName("корректную реакцию на неправильный ввод студента")
    void correctlyHandleWrongInputOfStudent() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(questionService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        doThrow(new NumberFormatException()).when(ioService).inputAnswer();
        assertDoesNotThrow(()->{
            assertThat(testService.testStudent()).isEqualTo(List.of(0));
        });
    }

    @Test
    @DisplayName("корректное отображение результатов теста")
    void correctlyShowsTestResults() {
        assertThat(testService.showResults(RIGHT)).containsIgnoringCase("passed").contains(String.valueOf(RIGHT));
        assertThat(testService.showResults(RIGHT - 1)).containsIgnoringCase("failed");
    }
    @Test
    @DisplayName("бросает исключение при невозможности открыть тест")
    void correctlyThrowsException() throws QuestionDaoException {
        doThrow(new QuestionDaoException("")).when(questionService).getAll();
        assertThrows(QuestionDaoException.class, testService::startTest);
    }
}