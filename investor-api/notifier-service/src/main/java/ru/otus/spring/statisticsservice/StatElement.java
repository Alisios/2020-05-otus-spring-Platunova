package ru.otus.spring.statisticsservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.time.LocalDateTime;

@KeySpace("mail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatElement {

    @Id
    private String id;

    @JsonProperty("Время начала отсчета")
    private LocalDateTime dateTime;

    @JsonProperty("Количество отправленных сообщений в телеграмме")
    private long telegramCount;

    @JsonProperty("Количество отправленных сообщений по электронной почте")
    private long mailCount;

    @JsonProperty("Количество случившихся ошибок")
    private long errors;

}
