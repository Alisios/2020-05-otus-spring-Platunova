package ru.otus.spring.service;

import org.springframework.http.ResponseEntity;
import ru.otus.spring.service.dto.SubscriptionDto;
import ru.otus.spring.service.dto.SubscriptionDtoFromUser;


import java.util.List;
import java.util.UUID;

public interface SubService {

    ResponseEntity<SubscriptionDto> save(SubscriptionDtoFromUser user);

    ResponseEntity<List<SubscriptionDto>> getAllSubs();

    ResponseEntity<SubscriptionDto> getById(UUID id);

    ResponseEntity<SubscriptionDto> getBTickerAndEvent(String ticker, String event);

    /**
     * удаление подписки со всеми подписчиками
     */
    ResponseEntity<String> deleteSub(String ticker, String event);

    /**
     * удаление подписчика
     */
    ResponseEntity<String> deleteUser(String ticker, String event, String id);
}
