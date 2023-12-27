package com.RedBus.Authentication.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDtoForUser {

    private String fullName;

    private Date dateOfBirth;
    private String email;
    private String mobile;


    private String userName;
    private String password;
}
