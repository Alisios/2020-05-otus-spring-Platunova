package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;

public interface TestHandler {


    User getUserFromInput();

    TestResult executeTest(User user);

    String printResultsOfTest(TestResult testResult);
}
