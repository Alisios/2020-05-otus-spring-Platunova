package ru.otus.spring.stockinfoservice.database.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * Информация которая приходит из кафки (может отличаться наличием некоторых
 * полей в зависимости от источника) на текущий момент времени
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Информация о компании, которая приходит из кафки (текущий момент времени)")
public class StockDtoCurrent {

    String id;

    @Schema(description = "Время")
    LocalDateTime date;

    @NotNull
    @Schema(description = "Название компании")
    String companyName;

    @NotNull
    @Schema(description = "Текущая стоимость")
    String lastCost;

    @Schema(description = "Тип ценной бумаги")
    String type;

    @Schema(description = "Тип компании компании")
    String sector;

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
    double adjClose;

    @Schema(description = "Текущее изменение цены по сравнению с последней сделкой вчерашнего закрытия")
    double change;

    @Schema(description = "Рейтинг компании")
    String bondCreditRating;
}