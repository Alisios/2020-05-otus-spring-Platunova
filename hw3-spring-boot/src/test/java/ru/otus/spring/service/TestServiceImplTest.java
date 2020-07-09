package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Тест проверяет: ")
class TestServiceImplTest {

    private final TestServiceProperties testServiceProperties = mock(TestServiceProperties.class);
    private final IOService ioService = mock(IOService.class);
    private final QuestionService questionService = mock(QuestionService.class);
    private final ConverterService converterService = mock(ConverterService.class);
    private final Localizer localizer = mock(Localizer.class);

    private TestHandler testService ;
    private final List<Question> list = List.of(
            new Question(123, "Что происходит?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 1));

    @BeforeEach
    void setUp(){
        Map<String, String> map = new HashMap<>();
        map.put("userName", "Введите имя");
        map.put("userSurname", "Введите фамилию ");
        map.put("infoBeforeTest", "Какая-то информация");
        map.put("errorInAnswer", "Информация об ошибке");
        map.put("isPassed", "is passed");
        map.put("resultOfTest", "Результаты ");
        map.put("isFailed", "is failed");
        when(localizer.getLocalizedTestServiceMessages()).thenReturn(map);
        when(testServiceProperties.getRightAnswersMin()).thenReturn(1);
        testService = new TestHandlerImpl(ioService, questionService, converterService,localizer, testServiceProperties);
    }

    @Test
    @DisplayName("корректный ввод-вывод перед тестом")
    void correctInputBeforeTest(){
        when(ioService.inputMessage()).thenReturn("Петров").thenReturn("Вася");
        testService.inputInfoBeforeTest();
        verify(ioService, times(2)).inputMessage();
        verify(ioService, times(3)).outputMessage(anyString());
    }

    @Test
    @DisplayName("корректное определение правильности ответа студента и вывод о сдаче теста")
    void correctlyDefinesRightAnswerOfStudentAndPassedResults() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(converterService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        when(ioService.inputMessage()).thenReturn("2");
        testService.testStudentAndGetResultScore();
        assertThat(testService.showResultsOfTest().contains("1"));
        assertThat(testService.showResultsOfTest()).containsIgnoringCase("passed")
                .contains(String.valueOf(testServiceProperties.getRightAnswersMin()));
    }

    @Test
    @DisplayName("корректное определение правильности ответа студента и вывод о несдаче теста")
    void correctlyDefinesRightAnswerOfStudentAndFailedReesults() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(converterService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        when(ioService.inputMessage()).thenReturn("3");
        testService.testStudentAndGetResultScore();
        assertThat(testService.showResultsOfTest()).doesNotContain("1");
        assertThat(testService.showResultsOfTest()).containsIgnoringCase("failed");
    }

    @Test
    @DisplayName("корректную реакцию на неправильный ввод студента")
    void correctlyHandleWrongInputOfStudent() throws QuestionDaoException {
        when(questionService.getAll()).thenReturn(list);
        when(converterService.convertQuestionsToString(list)).thenReturn(Collections.singletonList("123. Что происходит?\n \t1) a\n\t2) b\n\t3) c\n"));
        when(questionService.getRightAnswers(list)).thenReturn(List.of(1));
        doThrow(new NumberFormatException()).when(ioService).inputMessage();
        assertDoesNotThrow(()->{
            testService.testStudentAndGetResultScore();
            assertThat(testService.showResultsOfTest())
                    .isNotEqualTo(1);
        });
    }


    @Test
    @DisplayName("бросает исключение при невозможности открыть тест")
    void correctlyThrowsException() throws QuestionDaoException {
        doThrow(new QuestionDaoException("")).when(questionService).getAll();
        assertThrows(QuestionDaoException.class, testService::testStudentAndGetResultScore);
    }
}