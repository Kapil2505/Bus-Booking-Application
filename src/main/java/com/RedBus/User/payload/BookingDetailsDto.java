package com.RedBus.User.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsDto {
    private String bookingId;

    private String busCompanyName;


    private String passengerName;


    private String seatNumber;


    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime bookingTime;


    private String contactNumber;


    private String email;


    private boolean paymentStatus;

    // Additional details for a comprehensive booking entity

    private String fromCity;


    private String toCity;


    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date journeyDate;


    private double totalFare;


}
