package ru.otus.spring.stockinfoservice.database.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Информация которая отправляется для хранения в кэш
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CacheStockInfo {

    String ticker;

    LocalDateTime date;

    double open;

    double close;

    double low;

    double high;
}
