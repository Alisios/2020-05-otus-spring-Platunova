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

    public TestServiceImpl(IOService ioService, QuestionService questionService,  @Value("${service.rightAnswersMin}") int rightAnswersMin) {
        this.ioService = ioService;
        this.questionService = questionService;
        this.rightAnswersMin = rightAnswersMin;
    }

    @Override
    public void startTest() throws QuestionDaoException {
        ioService.outputQuestion("Напишите Вашу фамилию:");
        ioService.inputAnswer();
        ioService.outputQuestion("Напишите Ваше имя:");
        ioService.inputAnswer();
        ioService.outputQuestion("Вам будет предложен список вопросов и ответов к ним. Для того, чтобы ответить на вопрос введите номер ответа. Обращаем внимание, что при введении нечислового ответа, ответ не будет засчитан.");
        ioService.outputQuestion(showResults(testStudent().get(0)));
    }

    @Override
    public List<Integer> testStudent() throws QuestionDaoException {
        List<Integer> answerOfStudent = new ArrayList<>();
        answerOfStudent.add(0);
        List<Question> allQuestionsAndAnswers = questionService.getAll();
        List<String> allQuestionsAndAnswersString = questionService.convertQuestionsToString(allQuestionsAndAnswers);
        allQuestionsAndAnswersString.forEach((question) -> {
            ioService.outputQuestion(question);
            try {
                answerOfStudent.set(0, answerOfStudent.get(0) +
                        ((Integer.parseInt(ioService.inputAnswer())) == (questionService.getRightAnswers(allQuestionsAndAnswers)
                                .get(allQuestionsAndAnswersString.indexOf(question)) + 1) ? 1 : 0));
            }catch(NumberFormatException ex){
                ioService.outputQuestion("Ответ не засчитан. Ответ должен быть числом!");
            }
        });
        return answerOfStudent;
    }

    @Override
    public String showResults(int res){
        return "You have "+ res+ " scores. Test is " +((res >= rightAnswersMin) ? "passed." : "failed.");
    }
}
