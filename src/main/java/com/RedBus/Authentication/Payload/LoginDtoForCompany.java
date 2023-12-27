package com.RedBus.Authentication.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoForCompany {
    private String registrationNumberOrBusinessEmail;
    private String password;
}
