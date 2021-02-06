package ru.otus.spring.controllers;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Schema(description = "Модель ошибок")
@Builder
@Data
public class ErrorResponse {

    @Schema(description = "code")
    private int code;

    @Schema(description = "URI")
    private String uri;

    @Schema
    private String message;
}
