package ru.otus.spring.checkservice;

import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * Сообщение, которое будет послано пользователю
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInfoForUser {

    String id;

    String messageForUser;

    String email;

    String telegram;

}