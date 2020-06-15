package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao dao;

    @Override
    public List<Question> getAll() throws QuestionDaoException {
        return dao.findAll();
    }

    @Override
    public List<Integer> getRightAnswers(List<Question> allQuestionsAndAnswers) {
        List<Integer> listAnswers = new ArrayList<>();
        allQuestionsAndAnswers.forEach(question -> listAnswers.add(question.getRightAnswer()));
        return listAnswers;
    }

    @Override
    public List<String> convertQuestionsToString(List<Question> allQuestionsAndAnswers) {
        List<String> allQuestionsAndAnswersString = new ArrayList<>();
        allQuestionsAndAnswers.forEach((question) -> {
            allQuestionsAndAnswersString.add(question.getId() + ". " + question.getContentOfQuestion() + "\n" +
                    question.getListOfAnswers().stream()
                            .map(i -> ("\t" + (question.getListOfAnswers().indexOf(i) + 1) + ") " +
                                    i.getContentOfPossibleAnswer())).collect(Collectors.joining("\n")));
        });
        return allQuestionsAndAnswersString;
    }
}
