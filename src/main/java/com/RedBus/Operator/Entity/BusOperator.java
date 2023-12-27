package com.RedBus.Operator.Entity;


import com.RedBus.Operator.util.CustomLocalTimeDeserializer;
import com.RedBus.User.Entity.Booking;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bus_operators")
public class BusOperator {

    @Id
    @Column(name="bus_id")
    private String busId;

    @Column(name="bus_number")
    @NotBlank(message = "Bus number cannot be blank")
    @Pattern(regexp = "[A-Z0-9]+", message = "Bus number must consist of uppercase letters and numbers")
    private String busNumber;

    @Column(name="driver_name")
    @NotBlank(message = "Driver name cannot be blank")
    private String driverName;

    @Column(name="support_staff")
    private String supportStaff;

    @Column(name="bus_type")
    private String busType;

    @Column(name="number_seat")
    @Min(value = 20, message = "Number of seats must be at least 20")
    private int numberSeat;

    @Column(name="departure_city")
    private String departureCity;

    @Column(name="arrival_city")
    private String arrivalCity;

    @Column(name = "departure_time")
    //@JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime departureTime;


    @Column(name = "arrival_time")
   // @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime arrivalTime;

    @Column(name = "departure_date")
    //@JsonFormat(pattern = "MM/dd/yyyy")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "date always should be present or future")
    private Date departureDate;

    @Column(name = "arrival_date")
   // @JsonFormat(pattern = "MM/dd/yyyy")
    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "date always should be present or future")
    private Date arrivalDate;

    @Column(name="amenities")
    private String amenities;

    @Column(name="bus_operator_company_name")
    @NotBlank(message = "Company name cannot be blank")
    private String busOperatorCompanyName;

    @Column(name="total_travel_time")
    @Min(value = 0, message = "Total travel time must be at least 0")
    private double totalTravelTime;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="ticked_id",referencedColumnName = "ticket_id")
    private TicketPrice ticketPrice;

    @OneToMany(mappedBy = "busOperator",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Booking>bookings = new ArrayList<>();

}
