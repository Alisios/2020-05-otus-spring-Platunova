package ru.otus.spring.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.otus.spring.repository.DbException;

@ControllerAdvice
@Slf4j
public class CustomErrorHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Такая операция не предусмотрена: {}, {}.", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Операция не предусмотрена. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(Exception ex) {
        log.error("Такая страница не найдена. Проверьте корректность ссылки: {}, {}", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Такая страница не найдена. Проверьте корректность ссылки. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotEnoughFunds(NotFoundException ex) {
        return ResponseEntity.badRequest().body("Операция не выполнена. Объект с таким id не найден");
    }

    @ExceptionHandler(DbException.class)
    public ResponseEntity<String> handleDbException(DbException ex) {
        log.error("Ошибка при обработке в БД: {}, {}", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Попробуйте повторить. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

}
