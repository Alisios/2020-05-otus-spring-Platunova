package ru.otus.spring.commons.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException() { };

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
