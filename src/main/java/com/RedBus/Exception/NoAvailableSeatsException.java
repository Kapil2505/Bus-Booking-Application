package com.RedBus.Exception;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException(String message) {
        super(message);
    }
}

