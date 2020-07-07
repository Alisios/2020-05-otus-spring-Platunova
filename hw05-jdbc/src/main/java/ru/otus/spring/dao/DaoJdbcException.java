package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;

public class DaoJdbcException extends DataAccessException {

    public DaoJdbcException(String msg) {
        super(msg);
    }

    public DaoJdbcException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
