package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;

public interface TestService {

    void startTest() throws QuestionDaoException;
    int  testStudent() throws QuestionDaoException;
    String showResults(int res);
}
