package com.RedBus.User.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDetails {
    @NotBlank
    private String passengerName;

    @NotBlank
    private String seatNumber;

    @NotBlank
    private String contactNumber;

    private String email;
}
