package ru.otus.spring.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.Localizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class ParserCsv implements Parser {

    private final Localizer localizer;

    @Override
    public List<Question> parseQuestions() throws IOException {
        URL path = Thread.currentThread().getContextClassLoader().getResource(localizer.getLocalizedQuestionFile());
        String line = "";
        String cvsSplitBy = ";";
        List<Question> listOfQuestion = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(path).openStream()))) {
            while ((line = br.readLine()) != null) {
                try {
                    List<String> temp = Arrays.asList(line.split(cvsSplitBy));
                    listOfQuestion.add(new Question(Long.parseLong(temp.get(0)), temp.get(1),
                            Arrays.stream(temp.get(2).split(",")).map(Answer::new).collect(toList()), Integer.parseInt(temp.get(3))));

                } catch (RuntimeException e1) {
                    throw new RuntimeException("Problems with parsing of document: " + e1.getMessage());
                }
            }
            return listOfQuestion;
        } catch (IOException e2) {
            throw new IOException("The file is not found" + e2.getMessage());
        }
    }
}
