package ru.otus.spring.common;

public class StockDbServiceException extends RuntimeException {

    public StockDbServiceException() { };

    public StockDbServiceException(String msg) {
        super(msg);
    }

    public StockDbServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
