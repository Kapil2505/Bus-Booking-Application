package com.RedBus.Authentication.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDtoForOperator {

    private String companyName;


    private String registrationNumber;


    private String businessEmail;


    private String businessPhone;


    private String companyAddress;

    private String password;
}
