package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.User;

public interface TestHandler {


    User getUserFromInput();
    void testStudentAndGetResultScore() throws QuestionDaoException;
    String showResultsOfTest(User user);
}
