package ru.otus.spring.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import ru.otus.spring.helper.Parser;
import ru.otus.spring.helper.ParserCsv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionDaoImplTest {
    private static Logger logger = LoggerFactory.getLogger(QuestionDaoImplTest.class);

    private ClassPathXmlApplicationContext context;

    @BeforeEach
    void set() {
        context = new ClassPathXmlApplicationContext("/spring-context.xml");
    }

    @Test
    @DisplayName("корректную работы основных методов дао ")
    void correctWorkOfDao() throws QuestionDaoException {
        QuestionDaoImpl dao = context.getBean(QuestionDaoImpl.class);
        assertDoesNotThrow(dao::findAll);
        assertThat(dao.findAll()).isNotEmpty();
    }

    @Test
    @DisplayName("корректную работы основных методов дао при ошибках парсинга")
    void correctWorkOfDaoWhenParsingRuntimeException() throws QuestionDaoException {
        Resource resourceEr = context.getResource("classpath:/questions-and-answersError.csv");
        Parser parser = new ParserCsv(resourceEr);
        QuestionDao dao = new QuestionDaoImpl(parser);
        assertThrows(QuestionDaoException.class, dao::findAll);
    }

    @Test
    @DisplayName("корректную работы основных методов дао при отсутсвии файла")
    void correctWorkOfDaoWhenNullPointException() throws QuestionDaoException {
        Resource resourceEr = context.getResource("classpath:/questions-and-answersError5674.csv");
        Parser parser = new ParserCsv(resourceEr);
        QuestionDao dao = new QuestionDaoImpl(parser);
        assertThrows(QuestionDaoException.class, dao::findAll);
    }

    @AfterEach
    void close() {
        context.close();
    }

}