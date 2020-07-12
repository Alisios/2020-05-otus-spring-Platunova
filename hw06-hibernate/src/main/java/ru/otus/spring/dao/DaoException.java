package ru.otus.spring.dao;

public class DaoException extends RuntimeException {

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
