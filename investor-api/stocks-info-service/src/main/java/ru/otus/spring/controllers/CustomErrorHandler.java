package ru.otus.spring.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.otus.spring.commons.exceptions.ServiceException;
import ru.otus.spring.commons.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomErrorHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<String> handleAllExceptions(HttpRequestMethodNotSupportedException ex) {
        log.error("Такая операция не предусмотрена: {}, {}.", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Операция не предусмотрена. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public final ErrorResponse handleAllExceptions(HttpServletRequest httpServletRequest, RuntimeException ex) {
        return ErrorResponse.builder().code(-1).uri(httpServletRequest.getRequestURI()).message("Запрос не выполнен." + ". " + ex).build();
        //   log.error("Такая операция не предусмотрена: {}, {}.", ex.getCause(), ex.getMessage());
        // return ResponseEntity.badRequest().body("Запрос не выполнен. Операция не предусмотрена. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        log.error("Такая страница не найдена. Проверьте корректность ссылки: {}, {}", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Такая страница не найдена. Проверьте корректность ссылки. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidation(HttpServletRequest httpServletRequest, ValidationException ex) {
        log.error("Операция не выполнена так как пользователем введены некорректные данные", ex.getCause());
        return ErrorResponse.builder().code(-2).uri(httpServletRequest.getRequestURI()).message("Операция не выполнена так как пользователем введены некорректные данные" + ". " + ex).build();
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Операция не выполнена. Объект с таким id не найден");
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleDbException(ServiceException ex) {
        log.error("Ошибка при обработке в БД: {}, {}", ex.getCause(), ex.getMessage());
        return ResponseEntity.badRequest().body("Запрос не выполнен. Попробуйте повторить. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class, HttpClientErrorException.Forbidden.class})
    public ResponseEntity<String> handleForbiddenException(Throwable ex) {
        log.error("Ошибка доступа: {}, {}", ex.getCause(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет достаточных прав доступак данной операции. Exception: " + ex.getCause() + ". " + ex.getMessage());
    }
}
