package ru.otus.spring.handleservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 *  информация с биржи, дополненная типом события
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInfoRes {

    String id;

    LocalDateTime date;

    @JsonProperty("typeEvent")
    String typeEvent;

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

    double max;

    double min;
}
