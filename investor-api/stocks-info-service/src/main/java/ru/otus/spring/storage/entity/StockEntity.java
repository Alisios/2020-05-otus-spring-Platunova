package ru.otus.spring.storage.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "stocks")
//@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockEntity {

    @Field(name = "id")
    @Id
    String id;

    @Field(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime date;

    @Field(name = "name")
    String companyName;

    @Field(name = "type")
    String type;

    @Field(name = "ticker")
    String ticker;

    @Field(name = "exchange")
    String exchange;

    @Field(name = "open")
    double open;

    @Field(name = "close")
    double close;

    @Field(name = "low")
    double low;

    @Field(name = "high")
    double high;

    @Field(name = "volume")
    long volume;

    @Field(name = "adj_close")
    double adj_close;

    @Field(name = "bondCreditRating")
    String bondCreditRating;

}


