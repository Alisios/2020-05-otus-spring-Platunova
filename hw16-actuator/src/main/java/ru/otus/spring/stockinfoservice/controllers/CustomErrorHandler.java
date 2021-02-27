package ru.otus.spring.stockinfoservice.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.spring.common.StockDbServiceException;
import ru.otus.spring.common.ValidationException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomErrorHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(HttpRequestMethodNotSupportedException ex) {
        log.error("Ошибка при обработке запроса: {}, {}.", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Операция не предусмотрена. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }
    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<Object> handleValidation(HttpServletRequest httpServletRequest, ValidationException ex) {
        log.error("Операция не выполнена так как пользователем введены некорректные данные", ex.getCause());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Операция не предусмотрена. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

    @ExceptionHandler(StockDbServiceException.class)
    public ResponseEntity<String> handleDbException(StockDbServiceException ex) {
        log.error("Ошибка при обработке в БД: {}, {}", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Попробуйте повторить. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }
}
