package ru.otus.spring.commons.exceptions;

public class StockManagerException extends RuntimeException {

    public StockManagerException() {;}

    public StockManagerException(String msg) {
        super(msg);
    }

    public StockManagerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
