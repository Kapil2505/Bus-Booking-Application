package com.RedBus.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails>resourceNotFoundHandler(ResourceNotFoundException rs , WebRequest wb)
    {
        ErrorDetails err = new ErrorDetails(new Date(),rs.getMessage(),wb.getDescription(true));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails>globalExceptionHandler(Exception rs , WebRequest wb)
    {
        ErrorDetails err = new ErrorDetails(new Date(),rs.getMessage(),wb.getDescription(true));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<ErrorDetails> seatAlreadyBookedHandler(SeatAlreadyBookedException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAvailableSeatsException.class)
    public ResponseEntity<ErrorDetails> noAvailableSeatsHandler(NoAvailableSeatsException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorDetails> paymentFailedHandler(PaymentFailedException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingFailedException.class)
    public ResponseEntity<ErrorDetails> bookingFailedHandler(BookingFailedException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
