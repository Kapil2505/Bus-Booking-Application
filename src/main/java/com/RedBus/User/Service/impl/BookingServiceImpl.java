package com.RedBus.User.Service.impl;

import com.RedBus.Exception.*;
import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Repository.BusOperatorRepository;
import com.RedBus.User.Entity.Booking;
import com.RedBus.User.Repository.BookingRepository;
import com.RedBus.User.Service.BookingService;
import com.RedBus.User.payload.BookingDetailsDto;
import com.RedBus.User.payload.PassengerDetails;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    private BusOperatorRepository busOperatorRepository;
    private BookingRepository bookingRepository;

    private ModelMapper modelMapper;

    public BookingServiceImpl(BusOperatorRepository busOperatorRepository, BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.busOperatorRepository = busOperatorRepository;
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BookingDetailsDto mapToDto(Booking booking) {

        BookingDetailsDto dto = modelMapper.map(booking, BookingDetailsDto.class);
        return dto;
    }

    @Override
    public boolean existsBySeatNumber(String seatNumber) {
        return bookingRepository.existsBySeatNumber(seatNumber);
    }
    @Override
    public BookingDetailsDto createBooking(String busId, String promoCode, PassengerDetails passengerDetails) {
        BusOperator bus = busOperatorRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus details not found"));

        if (existsBySeatNumber(passengerDetails.getSeatNumber())) {
            // Handle seat already booked exception
            throw new SeatAlreadyBookedException("Seat number " + passengerDetails.getSeatNumber() + " is already booked.");
        }

        if (bus.getNumberSeat() > 0) {
            double ticketCost = (bus.getTicketPrice().getCode()).equals(promoCode) ?
                    bus.getTicketPrice().getTicketCostAfterDiscount() :
                    bus.getTicketPrice().getTicketCost();

            String payment = createPaymentIntent((int) ticketCost);

            if (payment != null) {
                Booking booking = new Booking();
                String bookingId = UUID.randomUUID().toString();
                booking.setBookingId(bookingId);
                booking.setBusOperator(bus);
                booking.setTicketId(bus.getTicketPrice().getTicketId());
                booking.setBusCompanyName(bus.getBusOperatorCompanyName());
                booking.setPassengerName(passengerDetails.getPassengerName());
                booking.setSeatNumber(passengerDetails.getSeatNumber());
                booking.setContactNumber(passengerDetails.getContactNumber());
                booking.setEmail(passengerDetails.getEmail());
                booking.setPaymentStatus(true); // Since payment was successful
                booking.setFromCity(bus.getDepartureCity());
                booking.setToCity(bus.getArrivalCity());
                booking.setJourneyDate(bus.getDepartureDate());
                booking.setTotalFare(ticketCost);
                booking.setChargeId(payment);

                try {
                    Booking bookingSaved = bookingRepository.save(booking);

                    if (bookingSaved != null) {
                        // Deduct NumberSeat
                        int remainingSeats = bus.getNumberSeat() - 1;
                        bus.setNumberSeat(remainingSeats);
                        busOperatorRepository.save(bus);
                    }

                    return mapToDto(bookingSaved);
                }  catch (Exception e) {
                    // Log the exception for further investigation
                    logger.error("Error during createBooking: {}", e.getMessage(), e);
                    throw new RuntimeException("Error during createBooking", e);
                }
            } else {
                // Handle payment failure exception
                throw new PaymentFailedException("Payment failed. Unable to create booking.");
            }
        } else {
            // Handle no available seats exception
            throw new NoAvailableSeatsException("No available seats for booking.");
        }
    }



    @Override
    public String createPaymentIntent(Integer amount) {
        Stripe.apiKey = stripeApiKey;
         try {

             PaymentIntent intent = PaymentIntent.create(new PaymentIntentCreateParams.Builder().setCurrency("usd").setAmount((long)amount*100).build());
            return generateResponse(intent.getClientSecret());

        } catch (StripeException e) {
             return generateResponse("Error creating PaymentIntent: " + e.getMessage());
         }

    }

    private String generateResponse(String clientSecret) {
        return  clientSecret;
    }



    /* ***************************************************************************************************************** */

    @Override
    public boolean cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.isCancellationStatus()) {
            // Booking is not already canceled, proceed with cancellation

          try {

              // Update NumberSeat for the bus
              BusOperator bus = booking.getBusOperator();
              bus.setNumberSeat(bus.getNumberSeat() + 1);
              busOperatorRepository.save(bus);

              booking.setSeatNumber(null);
              booking.setCancellationStatus(true);

              // Calculate refund amount and update refund details
              double refundAmount = calculateRefundAmount(bookingId);
              booking.setRefundAmount(refundAmount);



              // Save the updated booking entity
              bookingRepository.save(booking);

              return true; // Booking canceled successfully
          }
          catch (Exception e) {
              // Log the exception for further investigation
              logger.error("Error during cancelBooking: {}", e.getMessage(), e);
              throw new RuntimeException("Error during cancelBooking", e);
          }
        }

        return false; // Booking is already canceled
    }

    @Override
    public double calculateRefundAmount(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        LocalDateTime now = LocalDateTime.now();

        // Assuming getJourneyDate() returns a java.util.Date
        Date arrivalDate = booking.getJourneyDate();
        Instant instant = arrivalDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime arrivalDateTime = LocalDateTime.ofInstant(instant, zoneId);

        // Calculate the time difference in hours between the current time and the arrival date
        long hoursDifference = ChronoUnit.HOURS.between(now, arrivalDateTime);

        // Implement your refund logic based on the specified rules
        if (hoursDifference >= 24 * 6) {  // Before 11:30 pm one day before arrival day
            return booking.getTotalFare() * 0.90;  // Cut 10%
        } else if (hoursDifference >= 24 * 5 && hoursDifference < 24 * 6) {  // After 11:30 pm to before 11:30 am of arrival day
            return booking.getTotalFare() * 0.75;  // Cut 25%
        } else if (hoursDifference >= 24 * 3 && hoursDifference < 24 * 5) {  // After 11:30 am to before 3:30 pm of arrival day
            return booking.getTotalFare() * 0.50;  // Cut 50%
        } else if (hoursDifference >= 24 && hoursDifference < 24 * 3) {  // After 3:30 pm to before 7:30 pm of arrival day
            return booking.getTotalFare() * 0.25;  // Cut 75%
        } else if (hoursDifference >= 0 && hoursDifference < 24) {  // After 7:30 pm to before departure time
            return booking.getTotalFare() * 0.05;  // Cut 95%
        } else {
            return 0;  // After departure, no amount to be refunded
        }
    }

    @Override
    public void transferRefundToUserAccount(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.isCancellationStatus() && booking.getRefundAmount() > 0) {
            // Perform refund transfer using the Stripe API
            boolean transferSuccessful = performStripeRefundTransfer(booking.getChargeId(), booking.getRefundAmount());

            if (transferSuccessful) {
                System.out.println("Refund of $" + booking.getRefundAmount() + " transferred to user's account: " + booking.getChargeId());
            } else {
                // Handle the case where the refund transfer fails
                System.out.println("Refund transfer failed. Please contact customer support.");
            }
        }
    }

    private boolean performStripeRefundTransfer(String paymentIntentId, double refundAmount) {
        // Set your Stripe API key
        Stripe.apiKey = stripeApiKey;

        try {
            // Create a refund using the Stripe API
            Refund.create(new HashMap<String, Object>() {{
                put("payment_intent", paymentIntentId);
                put("amount", (int) (refundAmount * 100)); // Convert to cents
            }});

            return true; // Refund transfer successful
        } catch (StripeException e) {
            e.printStackTrace(); // Log the exception (handle it more gracefully in a production environment)
            return false; // Refund transfer failed
        }
    }
}
