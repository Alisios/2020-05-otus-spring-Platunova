package ru.otus.spring.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;

    private String userRealId;

    private String email;

    private String telegram;

    private double max;

    private double min;

    private double change;
}
