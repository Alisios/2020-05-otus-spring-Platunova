package ru.otus.spring.handleservice;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.models.StockInfoRes;

/**
 * Сервис предназначен для формирования сообщения пользователю в
 * зависимости от типа события
 */
@Data
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
final class MessageCreator {

    MessageProperties properties;

    String createMessage(StockInfoRes info) {
        String message;
        switch (info.getTypeEvent()) {
            case "event_1":
                message = properties.getChange().get("message") + " "
                        + info.getCompanyName() + " "
                        + properties.getChange().get("middle")
                        +  " " +info.getChange() + "%!";
                break;

            case "event_2":
                message = properties.getMax().get("message") + " "
                        + info.getCompanyName() + " "
                        + properties.getMax().get("middle")
                        + " " + info.getMax() + "!";
                break;

            case "event_3":
                message = properties.getMin().get("message") + " "
                        + info.getCompanyName() + " "
                        + properties.getMin().get("middle")
                        +  " " +info.getMin() + "!";
                break;

            default:
                throw new RuntimeException("Невозможно сформировать сообщение! Нет такого типа события!");
        }
        return message;
    }
}
