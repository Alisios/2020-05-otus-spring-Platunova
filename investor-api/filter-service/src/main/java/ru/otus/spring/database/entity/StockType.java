package ru.otus.spring.database.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@RedisHash("stock_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockType {

    @Id
    String id;

    @Indexed
    String ticker;

    String sector;

}
