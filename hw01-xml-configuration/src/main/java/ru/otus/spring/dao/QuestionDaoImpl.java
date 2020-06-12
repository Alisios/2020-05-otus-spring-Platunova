package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.spring.domain.Question;
import ru.otus.spring.helper.Parser;
import ru.otus.spring.helper.ParserCsv;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class QuestionDaoImpl implements QuestionDao {
    private static final Logger logger = LoggerFactory.getLogger(ParserCsv.class);
    private final Parser parserOfQuestions;

    public List<Question> findAll() throws QuestionDaoException {
        try {
            return parserOfQuestions.parseQuestions();
        } catch (IOException | RuntimeException e2) {
            logger.error("{} \n{}", e2.getMessage(), e2.getStackTrace());
            throw new QuestionDaoException(e2.getMessage());
        }
    }

}
