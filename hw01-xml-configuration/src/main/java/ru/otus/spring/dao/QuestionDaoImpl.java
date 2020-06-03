package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.spring.domain.Question;
import ru.otus.spring.helper.Parser;
import ru.otus.spring.helper.ParserCsv;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class QuestionDaoImpl implements QuestionDao {
    private static Logger logger = LoggerFactory.getLogger(ParserCsv.class);
    private final Parser parserOfQuestions;

    public List<Question> findAll() {
        try {
            return parserOfQuestions.parseQuestions();
        } catch (IOException | RuntimeException e2) {
            logger.error("{} \n{}", e2.getMessage(), e2.getStackTrace());
        }
        return Collections.emptyList();
    }

}
