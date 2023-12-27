package com.RedBus.Authentication.AuthEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="auth_bus_operator_company")
public class AuthBusOperatorCompany {

    @Id
    private String operatorId;
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "business_email")
    private String businessEmail;

    @Column(name = "business_phone")
    private String businessPhone;

    @Column(name = "company_address")
    private String companyAddress;

    private String password;


    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Role> roles;
}
