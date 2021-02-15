package ru.otus.spring.service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.otus.spring.service.dto.UserDto;

import java.util.Set;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDto {

    private UUID id;

    private String ticker;

    private String typeEvent;

    private Set<UserDto> users;


}
