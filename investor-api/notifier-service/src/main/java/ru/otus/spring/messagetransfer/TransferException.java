package ru.otus.spring.messagetransfer;

public class TransferException extends RuntimeException {

    public TransferException() { ;}

    public TransferException(String msg) {
        super(msg);
    }

    public TransferException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
