package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConverterServiceImpl implements ConverterService{

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
