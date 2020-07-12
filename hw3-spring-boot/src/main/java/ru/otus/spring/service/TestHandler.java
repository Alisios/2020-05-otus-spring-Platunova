package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;

public interface TestHandler {

    void inputInfoBeforeTest();
    void testStudentAndGetResultScore() throws QuestionDaoException;
    String showResultsOfTest();
}
