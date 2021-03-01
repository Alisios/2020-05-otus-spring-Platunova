package ru.otus.spring.handleservice.models;

import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;


/**
 * Информация которая приходит с биржи
 * поля могут быть по разному заполнены в зависимости от источника
 * дополненная типом и сектором
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInfoFull {

    String id;

    LocalDateTime date;

    String companyName;

    double lastCost;

    String type;

    String sector;

    String ticker;

    String exchange;

    double open;

    double close;

    double low;

    double high;

    long volume;

    double adjClose;

    double change;

    String bondCreditRating;
}