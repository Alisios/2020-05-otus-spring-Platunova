package ru.otus.spring.database;

public class TypeDbServiceException extends RuntimeException {

    public TypeDbServiceException() { };

    public TypeDbServiceException(String msg) {
        super(msg);
    }

    public TypeDbServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
