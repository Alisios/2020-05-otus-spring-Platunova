package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;

import java.util.List;

@Service
public class TestHandlerImpl implements TestHandler {

    private final IOService ioService;
    private final QuestionService questionService;
    private final ConverterService converterService;
    private final Localizer localizer;
    private final TestServiceProperties testServiceProperties;

    public TestHandlerImpl(IOService ioService, QuestionService questionService, ConverterService converterService, Localizer localizer, TestServiceProperties testServiceProperties) {
        this.ioService = ioService;
        this.questionService = questionService;
        this.converterService = converterService;
        this.localizer = localizer;
        this.testServiceProperties = testServiceProperties;
    }

    @Override
    public User getUserFromInput() {
        User user = new User();
        ioService.outputMessage(localizer.askUserForName());
        user.setName(ioService.inputMessage());
        ioService.outputMessage(localizer.askUserForSurname());
        user.setSurname(ioService.inputMessage());
        return user;
    }

    @Override
    public TestResult executeTest(User user) {
        TestResult testResult = new TestResult();
        ioService.outputMessage(localizer.printInfoBeforeTest());
        List<Question> allQuestionsAndAnswers = questionService.getAll();
        List<String> allQuestionsAndAnswersString = converterService.convertQuestionsToString(allQuestionsAndAnswers);
        testResult.setScore(allQuestionsAndAnswersString.stream().mapToInt(q -> {
            ioService.outputMessage(q);
            try {
                return Integer.parseInt(ioService.inputMessage()) ==
                        (questionService.getRightAnswers(allQuestionsAndAnswers)
                                .get(allQuestionsAndAnswersString.indexOf(q)) + 1) ? 1 : 0;
            } catch (NumberFormatException ex) {
                ioService.outputMessage(localizer.printErrorInTest());
            }
            return 0;
        }).sum());
        user.setIsTested(true);
        testResult.setUser(user);
        return testResult;
    }

    @Override
    public String printResultsOfTest(TestResult testResult) {
        testResult.setRes(localizer.printResultOfTest() + testResult.getScore() +
                ((testResult.getScore() >= testServiceProperties.getRightAnswersMin())
                        ? localizer.printSuccessResultOfTest()
                        : localizer.printFailedResultOfTest()));
        return testResult.toString();
    }

}
