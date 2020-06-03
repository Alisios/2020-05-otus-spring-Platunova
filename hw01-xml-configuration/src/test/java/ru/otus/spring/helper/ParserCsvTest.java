package ru.otus.spring.helper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoImpl;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты проверяют: ")
class ParserCsvTest {
    private static Logger logger = LoggerFactory.getLogger(ParserCsvTest.class);

    private ClassPathXmlApplicationContext context;

    @BeforeEach
    void set() {
        context = new ClassPathXmlApplicationContext("/spring-context.xml");
    }

    @Test
    @DisplayName("корректный парсинг и маппинг csv")
    void correctParsingAndMappingOfCsv() throws IOException {
        Parser parser = context.getBean(Parser.class);
        List<Question> parseQuestions = parser.parseQuestions();
        assertThat(parseQuestions).isNotEmpty();
        parseQuestions.forEach(answer -> {
            assertThat(answer.getListOfAnswers()).isNotEmpty();
            assertThat(answer.getListOfAnswers().size()).isEqualTo(3);
        });
        logger.info("{}", parser.parseQuestions());
    }

    @Test
    @DisplayName("бросание исключения при некорректной заполненности сsv")
    void correctThrowingOfExceptionOfIncorrectDateInCsv() {
        Resource resourceEr = context.getResource("classpath:/questions-and-answersError.csv");
        Parser parser = new ParserCsv(resourceEr);
        assertThrows(RuntimeException.class, parser::parseQuestions);
    }

    @Test
    @DisplayName("бросание исключения при отсуствии файла с данным имененм")
    void correctThrowingOfExceptionOfNullFile() {
        Resource resourceEr2 = context.getResource("classpath:/questions-and-answersError5674.csv");
        Parser parser2 = new ParserCsv(resourceEr2);
        assertThrows(Exception.class, parser2::parseQuestions);
    }

    @Test
    @DisplayName("корректную работы основных методов сервиса при ошибках парсинга")
    void correctWorkOfServiceWhenParsingRuntimeException() {
        Resource resourceEr = context.getResource("classpath:/questions-and-answersError.csv");
        Parser parser = new ParserCsv(resourceEr);
        QuestionDao dao = new QuestionDaoImpl(parser);
        assertDoesNotThrow(dao::findAll);
        assertThat(dao.findAll()).isEmpty();
    }

    @Test
    @DisplayName("корректную работы основных методов сервиса при отсутсвии файла")
    void correctWorkOfServiceWhenNullPointException() {
        Resource resourceEr = context.getResource("classpath:/questions-and-answersError5674.csv");
        Parser parser = new ParserCsv(resourceEr);
        QuestionDao dao = new QuestionDaoImpl(parser);
        assertDoesNotThrow(dao::findAll);
        assertThat(dao.findAll()).isEmpty();
    }

    @AfterEach
    void close() {
        context.close();
    }

}