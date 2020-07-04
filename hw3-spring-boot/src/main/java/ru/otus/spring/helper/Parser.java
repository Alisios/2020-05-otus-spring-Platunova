package ru.otus.spring.helper;

import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.util.List;

public interface Parser {

    List<Question> parseQuestions() throws IOException;

}
