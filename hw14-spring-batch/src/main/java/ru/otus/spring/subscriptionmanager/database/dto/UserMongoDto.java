package ru.otus.spring.subscriptionmanager.database.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "userssubs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMongoDto {
    @Field
    String id;

    @Field
    String user_real_id;

    @Field
    String email;

    @Field
    String telegram;

    @Field
    double max;

    @Field
    double min;

    @Field
    double change;
}
