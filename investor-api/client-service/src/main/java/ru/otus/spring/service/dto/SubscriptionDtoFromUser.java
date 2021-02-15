package ru.otus.spring.service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.otus.spring.service.dto.UserDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDtoFromUser {

    private UUID id;

    private String ticker;

    private String typeEvent;

    private UserDto userDto;
}
