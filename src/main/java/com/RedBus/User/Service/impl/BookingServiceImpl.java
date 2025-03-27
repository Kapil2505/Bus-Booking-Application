package com.RedBus.User.Service.impl;

import com.RedBus.Authentication.AuthEntity.AuthUsers;
import com.RedBus.Authentication.Configuration.JwtAuthenticationFilter;
import com.RedBus.Authentication.repository.AuthUsersRepository;
import com.RedBus.Exception.*;
import com.RedBus.Operator.Entity.BusOperator;
import com.RedBus.Operator.Repository.BusOperatorRepository;
import com.RedBus.Payment.PaymentService;
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

import javax.transaction.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {


    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private BusOperatorRepository busOperatorRepository;
    private BookingRepository bookingRepository;

    private ModelMapper modelMapper;

    private AuthUsersRepository userRepoo;

    private PaymentService paymentService;



    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public BookingServiceImpl(BusOperatorRepository busOperatorRepository, BookingRepository bookingRepository, ModelMapper modelMapper, AuthUsersRepository userRepoo, PaymentService paymentService) {
        this.busOperatorRepository = busOperatorRepository;
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
        this.userRepoo = userRepoo;
        this.paymentService = paymentService;
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
    @Transactional
    public BookingDetailsDto createBooking(String busId, String promoCode, PassengerDetails passengerDetails) {
        BusOperator bus = busOperatorRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus details not found"));

       String current_user = JwtAuthenticationFilter.User_Detail;
        AuthUsers user = userRepoo.findById(current_user).orElseThrow(() -> new ResourceNotFoundException("user not found !"));


        if (existsBySeatNumber(passengerDetails.getSeatNumber())) {
            // Handle seat already booked exception
            throw new SeatAlreadyBookedException("Seat number " + passengerDetails.getSeatNumber() + " is already booked.");
        }

        if (bus.getNumberSeat() > 0) {
            double ticketCost = (bus.getTicketPrice().getCode()).equals(promoCode) ?
                    bus.getTicketPrice().getTicketCostAfterDiscount() :
                    bus.getTicketPrice().getTicketCost();

            String payment = paymentService.createPaymentIntent((int) ticketCost);

            if (payment != null) {
                Booking booking = new Booking();
                String bookingId = UUID.randomUUID().toString();
                booking.setBookingId(bookingId);
                booking.setUser(user);
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


    /* ***************************************************************************************************************** */


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


    public double calculateRefundAmount(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        LocalTime cancelTime = LocalTime.now();
        LocalTime departureTime = booking.getBusOperator().getDepartureTime();
        long hoursDifference = Duration.between(cancelTime, departureTime).toHours();


        // Implement your refund logic based on the specified rules
        if (hoursDifference >= 24 ) {
            // Before 11:30 pm one day before arrival day
            double cut = booking.getTotalFare() * 0.20;
            return booking.getTotalFare()-cut;  // Cut 20%
        } else if (hoursDifference<24 && hoursDifference>15) {
            // After 11:30 pm to before 11:30 am of arrival day
            double cut = booking.getTotalFare() * 0.40;
            return booking.getTotalFare() -cut;  // Cut 40%

        }   else {
            return 0.0;  // After departure, no amount to be refunded
        }
    }


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



