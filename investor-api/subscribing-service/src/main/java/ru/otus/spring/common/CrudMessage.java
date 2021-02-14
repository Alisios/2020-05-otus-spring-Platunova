package ru.otus.spring.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionDtoFromUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CrudMessage {

    SubscriptionDtoFromUser subscriptionDto;

    CrudType type;

}
