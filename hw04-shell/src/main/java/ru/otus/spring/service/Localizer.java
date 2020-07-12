package ru.otus.spring.service;

import java.util.Map;

public interface Localizer {

    Map<String, String> getLocalisedMap();

    String askUserForName();

    String askUserForSurname();

    String printInfoBeforeTest();

    String printErrorInTest();

    String printResultOfTest();

    String printSuccessResultOfTest();

    String printFailedResultOfTest();

}
