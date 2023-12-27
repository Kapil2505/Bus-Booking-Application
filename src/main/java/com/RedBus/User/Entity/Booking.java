package com.RedBus.User.Entity;

import com.RedBus.Operator.Entity.BusOperator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bookings")
public class Booking {

    @Id
    private String bookingId;


    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name="bus_id")
    private BusOperator busOperator;

    @Column(name="ticket_id")
    private String ticketId;

    @NotNull
    @Column(name = "bus_company_name")
    private String busCompanyName;

    @NotNull
    @Column(name = "passenger_name")
    private String passengerName;

    private String seatNumber;

    @CreationTimestamp
    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @NotNull
    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "payment_status")
    private boolean paymentStatus;

    // Additional details for a comprehensive booking entity
    @NotNull
    @Column(name = "from_city")
    private String fromCity;

    @NotNull
    @Column(name = "to_city")
    private String toCity;

    @Column(name = "journey_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date journeyDate;

    @Column(name = "total_fare")
    private double totalFare;

    //  Booking Cancellation
    private boolean cancellationStatus;
    private double refundAmount;

    private String chargeId;
}
