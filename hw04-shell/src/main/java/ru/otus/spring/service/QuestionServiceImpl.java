package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;

    @Override
    public List<Question> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Integer> getRightAnswers(List<Question> allQuestionsAndAnswers) {
        List<Integer> listAnswers = new ArrayList<>();
        allQuestionsAndAnswers.forEach(question -> listAnswers.add(question.getRightAnswer()));
        return listAnswers;
    }

}
