package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.configs.TestServiceProperties;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocalizerImpl implements Localizer {

    private final TestServiceProperties testServiceProperties;
    private final MessageSource messageSource;

    @Override
    public  Map<String, String> getLocalisedMap(){
        return testServiceProperties.getLocaleMessagesKeys().stream().collect(Collectors.toMap((key) -> key, (key) -> messageSource.getMessage(key, null, testServiceProperties.getLocale())));
    }

    @Override
    public String askUserForName() {
        return getLocalisedMap().get("userName");
    }

    @Override
    public String askUserForSurname() {
        return getLocalisedMap().get("userSurname");
    }

    @Override
    public String printInfoBeforeTest() {
        return getLocalisedMap().get("infoBeforeTest");
    }

    @Override
    public String printErrorInTest() {
        return getLocalisedMap().get("errorInAnswer");
    }

    @Override
    public String printResultOfTest() {
        return getLocalisedMap().get("resultOfTest");
    }

    @Override
    public String printSuccessResultOfTest(){
        return getLocalisedMap().get("isPassed");
    }

    @Override
    public String printFailedResultOfTest(){
        return getLocalisedMap().get("isFailed");
    }
}
