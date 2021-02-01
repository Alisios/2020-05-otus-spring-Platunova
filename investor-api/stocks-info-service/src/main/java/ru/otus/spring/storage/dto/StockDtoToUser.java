package ru.otus.spring.storage.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Информация о компании, которая отправляется пользователю
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Информация о компании, которая отправляется пользователю")
public class StockDtoToUser {

    String id;

    @Schema(description = "Время")
    LocalDateTime date;

    @NotNull
    @Schema(description = "Название компании")
    String companyName;

    @Schema(description = "Тип компании компании")
    String type;

    @Schema(description = "Тикер")
    String ticker;

    @Schema(description = "Биржа")
    String exchange;

    @Schema(description = "Цена на момент открытия")
    double open;

    @Schema(description = "Цена на момент закрытия")
    double close;

    @Schema(description = "Наименьшая цена за день")
    double low;

    @Schema(description = "Наибольшая цена за день")
    double high;

    @Schema(description = "Количество проведенных операций")
    long volume;

    @Schema(description = "Скорректированная цена на момент закрытия")
    double adj_close;

    @Schema(description = "Рейтинг компании")
    String bondCreditRating;
}
