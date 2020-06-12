package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.helper.Parser;
import ru.otus.spring.helper.ParserCsv;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionDaoImplTest {

    private ClassPathXmlApplicationContext context;

    @Test
    @DisplayName("корректную работы основных методов дао ")
    void correctWorkOfDao() throws QuestionDaoException, IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("questions-and-answers.csv");// "";
        Parser parser = new ParserCsv(Objects.requireNonNull(path).openStream());
        QuestionDao dao = new QuestionDaoImpl(parser);
        assertDoesNotThrow(()->{
            assertThat(dao.findAll()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("корректную работы основных методов дао при ошибках парсинга")
    void correctWorkOfDaoWhenParsingRuntimeException() throws QuestionDaoException, IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("questions-and-answersError.csv");// "";
        assertThrows(QuestionDaoException.class, ()->{
            Parser parser = new ParserCsv(Objects.requireNonNull(path).openStream());
            QuestionDao dao = new QuestionDaoImpl(parser);
            dao.findAll();
        });
    }

    @Test
    @DisplayName("корректную работы основных методов дао при отсутсвии файла")
    void correctWorkOfDaoWhenNullPointException() throws QuestionDaoException, IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("questions-and-answersError5674.csv");// "";
        assertThrows(NullPointerException.class, ()->{
            Parser parser = new ParserCsv(Objects.requireNonNull(path).openStream());
            QuestionDao dao = new QuestionDaoImpl(parser);
            dao.findAll();
        });
    }

}