package com.RedBus.User.Service;

import com.RedBus.User.Entity.Booking;
import com.RedBus.User.payload.BookingDetailsDto;
import com.RedBus.User.payload.PassengerDetails;

public interface BookingService {

    BookingDetailsDto mapToDto(Booking booking);
    boolean existsBySeatNumber(String seatNumber);
    BookingDetailsDto createBooking(String busId , String promoCode , PassengerDetails passengerDetails);
    public String createPaymentIntent(Integer amount);
    // Inside BookingService Interface
    boolean cancelBooking(String bookingId);
    double calculateRefundAmount(String bookingId);
    // Inside BookingService Interface
    void transferRefundToUserAccount(String bookingId);


}
