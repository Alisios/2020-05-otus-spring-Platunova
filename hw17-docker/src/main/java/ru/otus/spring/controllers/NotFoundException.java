package ru.otus.spring.controllers;

class NotFoundException extends RuntimeException {

    NotFoundException() {
    }

    NotFoundException(String msg) {
        super(msg);
    }

    NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
