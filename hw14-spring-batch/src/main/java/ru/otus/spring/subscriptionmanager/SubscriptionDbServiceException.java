package ru.otus.spring.subscriptionmanager;

public class SubscriptionDbServiceException extends RuntimeException {

    public SubscriptionDbServiceException() { };

    public SubscriptionDbServiceException(String msg) {
        super(msg);
    }

    public SubscriptionDbServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
