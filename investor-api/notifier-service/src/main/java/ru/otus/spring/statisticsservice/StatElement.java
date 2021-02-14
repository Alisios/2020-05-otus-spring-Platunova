package ru.otus.spring.statisticsservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.time.LocalDateTime;

@KeySpace("mail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatElement {

    @Id
    String id;

    @JsonProperty("Время начала отсчета")
    LocalDateTime dateTime;

    @JsonProperty("Количество отправленных сообщений в телеграмме")
    long telegramCount;

    @JsonProperty("Количество отправленных сообщений по электронной почте")
    long mailCount;

    @JsonProperty("Количество случившихся ошибок")
    long errors;

}
