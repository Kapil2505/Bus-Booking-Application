package com.RedBus.Exception;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}

