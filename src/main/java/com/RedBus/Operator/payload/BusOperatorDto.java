package com.RedBus.Operator.payload;


import com.RedBus.Operator.Entity.TicketPrice;
import com.RedBus.Operator.util.CustomLocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorDto {
    private String busId;
    private String busNumber;
    private String driverName;
    private String supportStaff;
    private String busType;
    private int numberSeat;
    private String departureCity;
    private String arrivalCity;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;


    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date departureDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date arrivalDate;

    private String amenities;
    private String busOperatorCompanyName;
    private double totalTravelTime;

   private TicketPrice ticketPrice;

}
