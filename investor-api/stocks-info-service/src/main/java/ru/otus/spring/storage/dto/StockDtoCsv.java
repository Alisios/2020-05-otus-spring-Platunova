package ru.otus.spring.storage.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * Информация которая загружается из csv через Spring Batch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockDtoCsv {

    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyyMMdd")
    // LocalDateTime date;
    String date;

    // @DateTimeFormat(iso = DateTimeFormat.ISO.TIME,pattern ="HHmmss")
    // LocalDateTime time;
    String time;
    String per;
    String ticker;

    double open;

    double close;

    double low;

    double high;

    long volume;
}
