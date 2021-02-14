package ru.otus.spring.checkservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * Обработанная информация,
 * которая требуется для уточнения при посылке сообщения пользователю
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultInfo {

    String id;
    /**
     * тип подписки
     */

    @JsonProperty("typeEvent")
    String typeEvent;

    /**
     * уникальное обозначение компании на бирже
     */
    String ticker;

    /**
     * сообщение пользователю
     */
    String message;

    /**
     * текущий максимум цены, который использовался при определении наличия события
     */
    double max;

    /**
     * текущий минимум цены, который использовался при определении наличия события
     */
    double min;

    /**
     * цена последеней совершенной сделки
     */
    double lastCost;

    /**
     * изменение цены по сравнению с последеней совершенной сделкой предыдущего дня
     */
    double change;
}