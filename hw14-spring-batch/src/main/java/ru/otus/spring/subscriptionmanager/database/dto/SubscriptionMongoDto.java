package ru.otus.spring.subscriptionmanager.database.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "subs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionMongoDto {

    @Field
    String id;

    @Field
    String ticker;

    @Field
    String typeEvent;

    @Field
    Set<UserMongoDto> users;
}
