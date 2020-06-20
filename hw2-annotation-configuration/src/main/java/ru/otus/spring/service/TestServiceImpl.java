package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestServiceImpl implements TestService{

    private final int rightAnswersMin;
    private final IOService ioService;
    private final QuestionService questionService;
    private final ConverterService converterService;

    public TestServiceImpl(IOService ioService, QuestionService questionService,  @Value("${service.rightAnswersMin}") int rightAnswersMin, ConverterService converterService) {
        this.ioService = ioService;
        this.questionService = questionService;
        this.rightAnswersMin = rightAnswersMin;
        this.converterService = converterService;
    }

    @Override
    public void startTest() throws QuestionDaoException {
        ioService.outputMessage("Напишите Вашу фамилию:");
        ioService.inputMessage();
        ioService.outputMessage("Напишите Ваше имя:");
        ioService.inputMessage();
        ioService.outputMessage("Вам будет предложен список вопросов и ответов к ним. Для того, чтобы ответить на вопрос введите номер ответа. Обращаем внимание, что при введении нечислового ответа, ответ не будет засчитан.");
        ioService.outputMessage(showResults(testStudent()));
    }

    @Override
    public int testStudent() throws QuestionDaoException {
        List<Integer> answerOfStudent = new ArrayList<>();
        answerOfStudent.add(0);
        List<Question> allQuestionsAndAnswers = questionService.getAll();
        List<String> allQuestionsAndAnswersString = converterService.convertQuestionsToString(allQuestionsAndAnswers);
        allQuestionsAndAnswersString.forEach((question) -> {
            ioService.outputMessage(question);
            try {
                answerOfStudent.set(0, answerOfStudent.get(0) +
                        ((Integer.parseInt(ioService.inputMessage())) == (questionService.getRightAnswers(allQuestionsAndAnswers)
                                .get(allQuestionsAndAnswersString.indexOf(question)) + 1) ? 1 : 0));
            }catch(NumberFormatException ex){
                ioService.outputMessage("Ответ не засчитан. Ответ должен быть числом!");
            }
        });
        return answerOfStudent.get(0);
    }

    @Override
    public String showResults(int res){
        return "You have "+ res+ " scores. Test is " +((res >= rightAnswersMin) ? "passed." : "failed.");
    }
}
