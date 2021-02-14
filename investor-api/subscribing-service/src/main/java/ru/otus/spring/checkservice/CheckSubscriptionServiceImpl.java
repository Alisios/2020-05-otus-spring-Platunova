package ru.otus.spring.checkservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.subscriptionmanager.SubscriptionManager;
import ru.otus.spring.subscriptionmanager.database.SubscriptionDbService;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDto;
import ru.otus.spring.subscriptionmanager.database.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * сервис для проверки наличия подписки на событие и
 * отправка сервису отправки пользователю
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CheckSubscriptionServiceImpl implements CheckSubscriptionService {

    SubscriptionManager subscriptionManager;
    MessageProperties properties;

    @Override
    public List<StockInfoForUser> checkAndSend(ResultInfo info) {
        Optional<SubscriptionDto> sub = subscriptionManager
                .getByTickerAndEventType(info.getTicker(), info.getTypeEvent());
        return sub.map(subscription -> subscription.getUsers().stream()
                .filter(userDto -> checkEvent(userDto, info))
                .map(e -> StockInfoForUser.builder()
                        .id(UUID.randomUUID().toString())
                        .messageForUser(info.getMessage())
                        .email(e.getEmail())
                        .telegram(e.getTelegram())
                        .build())
                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private boolean checkEvent(UserDto sub, ResultInfo info) {
        if (info.getTypeEvent().equals(properties.getEventType().get("type1")))
            return info.getChange() > sub.getChange();
        else if (info.getTypeEvent().equals(properties.getEventType().get("type2")))
            return info.getLastCost() > sub.getMax();
        else if (info.getTypeEvent().equals(properties.getEventType().get("type3")))
            return info.getLastCost() < sub.getMin();
        else return false;
    }

}
