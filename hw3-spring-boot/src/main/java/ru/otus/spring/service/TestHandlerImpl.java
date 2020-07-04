package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.configs.TestServiceProperties;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;

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
    private final Map<String, String> localeMessagesMap;
    private final AtomicInteger res = new AtomicInteger();


    public TestHandlerImpl(IOService ioService, QuestionService questionService, ConverterService converterService, Localizer localizer, TestServiceProperties testServiceProperties) {
        this.ioService = ioService;
        this.questionService = questionService;
        this.converterService = converterService;
        this.localizer = localizer;
        this.testServiceProperties = testServiceProperties;
        localeMessagesMap = localizer.getLocalizedTestServiceMessages();
    }

    @Override
    public void inputInfoBeforeTest() {
        ioService.outputMessage(localeMessagesMap.get("userName"));
        ioService.inputMessage();
        ioService.outputMessage(localeMessagesMap.get("userSurname"));
        ioService.inputMessage();
        ioService.outputMessage(localeMessagesMap.get("infoBeforeTest"));
    }

    @Override
    public void testStudentAndGetResultScore() throws QuestionDaoException {
        List<Question> allQuestionsAndAnswers = questionService.getAll();
        List<String> allQuestionsAndAnswersString = converterService.convertQuestionsToString(allQuestionsAndAnswers);
        res.set(allQuestionsAndAnswersString.stream().mapToInt(q -> {
            ioService.outputMessage(q);
            try {
                return Integer.parseInt(ioService.inputMessage()) ==
                        (questionService.getRightAnswers(allQuestionsAndAnswers)
                                .get(allQuestionsAndAnswersString.indexOf(q)) + 1) ? 1 : 0;
            } catch (NumberFormatException ex) {
                ioService.outputMessage(localeMessagesMap.get("errorInAnswer"));
            }
            return 0;
        }).sum());
    }

    @Override
    public String showResultsOfTest() {
        return localeMessagesMap.get("resultOfTest") + res.get() +
                ((res.get() >= testServiceProperties.getRightAnswersMin())
                        ? localeMessagesMap.get("isPassed")
                        : localeMessagesMap.get("isFailed"));
    }

}
