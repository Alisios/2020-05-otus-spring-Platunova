package ru.otus.spring.subscriptionmanager.database.dto;

import org.springframework.stereotype.Component;
import ru.otus.spring.subscriptionmanager.database.entities.Subscription;
import ru.otus.spring.subscriptionmanager.database.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер для миграции сущностец между postgresql и mongo
 */
@Component
public class SubMapper {

    public SubscriptionMongoDto mapFromSqlToMongoDto(Subscription sub) {
        Set<User> users = sub.getUsers();
        return SubscriptionMongoDto.builder().id(sub.getId().toString())
                .ticker(sub.getTicker())
                .typeEvent(sub.getTypeEvent())
                .users(users.stream().map(this::mapFromSqlToMongoDto).collect(Collectors.toSet()))
                .build();
    }

    public UserMongoDto mapFromSqlToMongoDto(User user) {
        return UserMongoDto.builder()
                .id(user.getId().toString())
                .change(user.getChange())
                .email(user.getEmail())
                .max(user.getMax())
                .min(user.getMin())
                .telegram(user.getTelegram())
                .userRealId(user.getUserRealId())
                .build();
    }
}
