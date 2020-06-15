package ru.otus.spring.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParserCsv implements Parser {

    private InputStream resource;

    @Override
    public List<Question> parseQuestions() throws IOException {
        String line = "";
        String cvsSplitBy = ";";
        List<Question> listOfQuestion = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
            while ((line = br.readLine()) != null) {
                try {
                    List<String> temp = Arrays.asList(line.split(cvsSplitBy));
                    listOfQuestion.add(new Question(Long.parseLong(temp.get(0)), temp.get(1),
                            Arrays.stream(temp.get(2).split(",")).map(Answer::new).collect(toList())));

                } catch (RuntimeException e1) {
                    throw new RuntimeException("Problems with parsing of document: " + e1.getMessage());
                }
            }
            return listOfQuestion;
        } catch (IOException | NullPointerException e2) {
            throw new IOException("The file is not found" + e2.getMessage());
        }
    }
}
