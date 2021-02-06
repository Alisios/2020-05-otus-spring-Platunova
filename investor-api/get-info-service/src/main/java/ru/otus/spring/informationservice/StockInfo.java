package ru.otus.spring.informationservice;

import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;


/**
 * Информация которая приходит с биржи
 * поля могут быть по разному заполнены в зависимости от источника
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInfo {

    String id;

    LocalDateTime date;

    String companyName;

    String type;

    String ticker;

    String exchange;

    double open;

    double close;

    double low;

    double high;

    long volume;

    double adjClose;

    String bondCreditRating;
}