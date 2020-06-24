package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Question;
import ru.otus.spring.helper.Parser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class QuestionDaoImplTest {
    private final Parser  parser = mock(Parser.class);
    private final QuestionDao dao = new QuestionDaoImpl(parser);

    @Test
    @DisplayName("корректную работы основных методов дао ")
    void correctWorkOfDao() throws IOException {
        List<Question> list = List.of(new Question(123, "Что происходит?", Collections.emptyList(),1));
        when(parser.parseQuestions()).thenReturn(list);
        assertDoesNotThrow(()->{
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