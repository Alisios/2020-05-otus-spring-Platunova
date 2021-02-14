package ru.otus.spring.service;

public class RestException extends RuntimeException {

    public RestException() { };

    public RestException(String msg) {
        super(msg);
    }

    public RestException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
