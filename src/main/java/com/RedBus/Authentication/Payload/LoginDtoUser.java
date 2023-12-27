package com.RedBus.Authentication.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class LoginDtoUser {
    private String usernameOrEmail;
    private String password;
}
