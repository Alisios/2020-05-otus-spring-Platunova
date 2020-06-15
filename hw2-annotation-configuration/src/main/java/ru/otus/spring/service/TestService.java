package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;

import java.util.List;

public interface TestService {

    void startTest() throws QuestionDaoException;
    List<Integer> testStudent() throws QuestionDaoException;
    String showResults(int res);
}
