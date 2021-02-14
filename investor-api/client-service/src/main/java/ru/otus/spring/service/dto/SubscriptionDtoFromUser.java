package ru.otus.spring.service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.otus.spring.service.dto.UserDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionDtoFromUser {

    private UUID id;

    String ticker;

    String typeEvent;

    UserDto userDto;
}
