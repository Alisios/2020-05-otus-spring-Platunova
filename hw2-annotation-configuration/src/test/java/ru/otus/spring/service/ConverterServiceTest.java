package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Тест проверяет: ")
public class ConverterServiceTest {
    private final ConverterService converterService = new ConverterServiceImpl();
    private final List<Question> list = List.of(
            new Question(123, "Что происходит?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 1),
            new Question(124, "А сейчас?",
                    List.of(new Answer("a"), new Answer("b"), new Answer("c")), 2));
    private final List<String> listString = List.of("123. Что происходит?\n" +
            "\t1) a\n\t2) b\n\t3) c", "124. А сейчас?\n\t1) a\n\t2) b\n\t3) c");

    @Test
    @DisplayName("корректное преобразование списка вопросов в строку")
    void correctlyConvertQuestionListInStringList() {
        assertThat(converterService.convertQuestionsToString(list)).isEqualTo(listString);
    }
}
