package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

public interface ConverterService {

    List<String> convertQuestionsToString(List<Question> allQuestionsAndAnswers);
}
