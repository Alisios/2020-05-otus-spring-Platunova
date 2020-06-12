package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;

import java.io.PrintStream;
import java.util.List;

@AllArgsConstructor
public class OutputServiceImpl implements OutputService {

    private final  QuestionService service;
    private final  PrintStream outPut;

    public void showQuestions() throws QuestionDaoException {
        List<Question> allQuestionsAndAnswers = service.getAll();
        allQuestionsAndAnswers.forEach((question) -> {
            outPut.println(question.getId() + ") " +
                    question.getContentOfQuestion());
            question.getListOfAnswers().forEach(answer -> outPut.println("- " + answer.getContentOfPossibleAnswer()));
        });
    }
}
