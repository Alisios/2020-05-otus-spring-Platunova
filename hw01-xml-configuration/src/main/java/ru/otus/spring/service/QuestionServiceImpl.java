package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.List;

@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao dao;

    @Override
    public List<Question> getAll() {
        return dao.findAll();
    }
}
