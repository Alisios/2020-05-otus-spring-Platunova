package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import ru.otus.spring.dao.QuestionDaoException;
import ru.otus.spring.domain.Question;

import java.util.List;

@AllArgsConstructor
public class PrintResultServiceImpl implements PrintResultService {

    private final QuestionService service;

    @Override
    public String printResult() throws QuestionDaoException {
        List<Question> allQuestionsAndAnswers = service.getAll();
        StringBuilder sb = new StringBuilder();
        allQuestionsAndAnswers.forEach((question) -> {
            sb.append(question.getId())
                    .append(") ")
                    .append(question.getContentOfQuestion())
                    .append("\n");
            question.getListOfAnswers().forEach(
                    answer -> sb.append("- ")
                            .append(answer.getContentOfPossibleAnswer())
                    .append("\n"));;
        });
        return sb.toString();
    }
}
