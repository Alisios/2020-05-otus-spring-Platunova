package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestHandlerImpl implements TestHandler{

    private final IOService ioService;
    private final QuestionService questionService;
    private final ConverterService converterService;
    private final Localizer localizer;
    private final TestServiceProperties testServiceProperties;
    private final AtomicInteger res = new AtomicInteger();


    public TestHandlerImpl(IOService ioService, QuestionService questionService, ConverterService converterService, Localizer localizer, TestServiceProperties testServiceProperties) {
        this.ioService = ioService;
        this.questionService = questionService;
        this.converterService = converterService;
        this.localizer = localizer;
        this.testServiceProperties = testServiceProperties;
    }

    @Override
    public User getUserFromInput(){
        User user = new User();
        ioService.outputMessage(localizer.getLocalizedTestServiceMessages().get("userName"));
        user.setName(ioService.inputMessage());
        ioService.outputMessage(localizer.getLocalizedTestServiceMessages().get("userSurname"));
        user.setSurname(ioService.inputMessage());
        return user;
    }

    private void inputInfoBeforeTest() {
        ioService.outputMessage(localizer.getLocalizedTestServiceMessages().get("infoBeforeTest"));
    }

    @Override
    public void testStudentAndGetResultScore() throws QuestionDaoException {
        inputInfoBeforeTest();
        List<Question> allQuestionsAndAnswers = questionService.getAll();
        List<String> allQuestionsAndAnswersString = converterService.convertQuestionsToString(allQuestionsAndAnswers);
        res.set(allQuestionsAndAnswersString.stream().mapToInt(q -> {
            ioService.outputMessage(q);
            try {
                return Integer.parseInt(ioService.inputMessage()) ==
                        (questionService.getRightAnswers(allQuestionsAndAnswers)
                                .get(allQuestionsAndAnswersString.indexOf(q)) + 1) ? 1 : 0;
            } catch (NumberFormatException ex) {
                ioService.outputMessage(localizer.getLocalizedTestServiceMessages().get("errorInAnswer"));
            }
            return 0;
        }).sum());
    }

    @Override
    public String showResultsOfTest() {
        return localizer.getLocalizedTestServiceMessages().get("resultOfTest") + res.get() +
                ((res.get() >= testServiceProperties.getRightAnswersMin())
                        ? localizer.getLocalizedTestServiceMessages().get("isPassed")
                        : localizer.getLocalizedTestServiceMessages().get("isFailed"));
    }

}
