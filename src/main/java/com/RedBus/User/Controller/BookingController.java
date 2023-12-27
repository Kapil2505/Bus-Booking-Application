package com.RedBus.User.Controller;

import com.RedBus.Exception.*;
import com.RedBus.User.Service.BookingService;
import com.RedBus.User.payload.BookingDetailsDto;
import com.RedBus.User.payload.PassengerDetails;
import com.RedBus.util.EmailService;
import com.RedBus.util.PdfService; // Import your PdfService
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking/")
@Tag(name = "Booking and Cancellation APIs",description = "this apis used for booking and cancelling tickets")
public class BookingController {

    private BookingService bookingService;
    private EmailService emailService;
    private PdfService pdfService;

    public BookingController(BookingService bookingService, EmailService emailService, PdfService pdfService) {
        this.bookingService = bookingService;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "post operation to book ticket")
    @PostMapping("/bookBus")
    public ResponseEntity<?> bookBus(
            @RequestParam("busId") String busId,
            @RequestParam("promoCode") String promoCode,
            @RequestBody PassengerDetails passengerDetails) {

        try {
            BookingDetailsDto booking = bookingService.createBooking(busId, promoCode, passengerDetails);

            if (booking != null) {
                // Generate PDF and attach it to the email
                byte[] pdfBytes = pdfService.generatePdf(booking);

                // Send email with PDF attachment
                emailService.sendMailWithAttachment(
                        booking.getEmail(),
                        "Booking is confirmed. Booking ID: " + booking.getBookingId(),
                        "Your booking is confirmed.\nName: " + booking.getPassengerName(),
                        "booking_details.pdf", pdfBytes);

                return new ResponseEntity<>(booking, HttpStatus.CREATED);
            }

            // Handle the case where booking is not created
            return new ResponseEntity<>("Seats are not available", HttpStatus.OK);

        } catch (SeatAlreadyBookedException e) {
            // Handle seat already booked exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (NoAvailableSeatsException e) {
            // Handle no available seats exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (PaymentFailedException e) {
            // Handle payment failure exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (BookingFailedException e) {
            // Handle booking failure due to database error
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (ResourceNotFoundException e) {
            // Handle bus details not found exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "put operation to cancel ticket")
    @PutMapping("/cancelBooking/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingId) {
        try {
            boolean canceled = bookingService.cancelBooking(bookingId);

            if (canceled) {
                // Transfer refund to the user's account
                bookingService.transferRefundToUserAccount(bookingId);

                return new ResponseEntity<>("Booking canceled successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Booking is already canceled", HttpStatus.OK);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
