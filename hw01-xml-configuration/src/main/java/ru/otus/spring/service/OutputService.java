package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;

public interface OutputService {

    void showQuestions() throws QuestionDaoException;
}
