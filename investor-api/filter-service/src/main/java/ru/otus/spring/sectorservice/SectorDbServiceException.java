package ru.otus.spring.sectorservice;

public class SectorDbServiceException extends RuntimeException {

    public SectorDbServiceException() { };

    public SectorDbServiceException(String msg) {
        super(msg);
    }

    public SectorDbServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
