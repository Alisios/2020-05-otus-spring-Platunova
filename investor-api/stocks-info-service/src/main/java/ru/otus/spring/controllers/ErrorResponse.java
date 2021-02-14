package ru.otus.spring.controllers;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Schema(description = "Модель ошибок")
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    @Schema(description = "code")
    int code;

    @Schema(description = "URI")
    String uri;

    @Schema
    String message;
}
