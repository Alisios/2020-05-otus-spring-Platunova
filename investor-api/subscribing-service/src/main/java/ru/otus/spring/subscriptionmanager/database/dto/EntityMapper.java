package ru.otus.spring.subscriptionmanager.database.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.spring.subscriptionmanager.database.entities.User;
import ru.otus.spring.subscriptionmanager.database.entities.Subscription;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "subscriptions", ignore = true)
    User dtoToUser(UserDto subscriberDto);

    //@Mapping(target = "subscriptions", ignore = true)
    UserDto userToDto(User user);

    default Subscription fromSubUserDtoToSub(SubscriptionDtoFromUser subscriptionDtoFromUser) {
        return Subscription.builder()
                .id(UUID.randomUUID())
                .ticker(subscriptionDtoFromUser.getTicker())
                .typeEvent(subscriptionDtoFromUser.getTypeEvent())
                .build();
    }

    default SubscriptionDto subToDto(Subscription subscription) {
        Set<UserDto> set = subscription.getUsers().stream().map(this::userToDto).collect(Collectors.toSet());
        return SubscriptionDto.builder()
                .id(UUID.randomUUID())
                .ticker(subscription.getTicker())
                .users(set)
                .typeEvent(subscription.getTypeEvent())
                .build();
    }

}
