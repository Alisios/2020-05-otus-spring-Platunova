package ru.otus.spring.typeservice;

public class TypeServiceException extends RuntimeException {

    public TypeServiceException() {
    }

    public TypeServiceException(String msg) {
        super(msg);
    }

    public TypeServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}