package ru.otus.spring.service;

import java.util.Map;

public interface Localizer {

    Map<String, String> getLocalizedTestServiceMessages();

    String getLocalizedQuestionFile();
}
