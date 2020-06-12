package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;

public interface PrintResultService {
    String printResult() throws QuestionDaoException;
}
