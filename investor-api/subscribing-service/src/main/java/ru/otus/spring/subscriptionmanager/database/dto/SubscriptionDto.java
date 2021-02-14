package ru.otus.spring.subscriptionmanager.database.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionDto {

    private UUID id;

    String ticker;

    String typeEvent;

    Set<UserDto> users;


}
