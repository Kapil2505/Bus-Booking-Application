package com.RedBus.User.Repository;

import com.RedBus.User.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,String> {
    boolean existsBySeatNumber(String seatNumber);
}
