package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDaoException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestHandler testHandler;
    private final IOService ioService;

    @Override
    public void startTest() throws QuestionDaoException {
        testHandler.inputInfoBeforeTest();
        testHandler.testStudentAndGetResultScore();
        ioService.outputMessage(testHandler.showResultsOfTest());
    }
}
