package com.RedBus.Exception;

public class BookingFailedException extends RuntimeException {
    public BookingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

