package com.RedBus.Authentication.repository;

import com.RedBus.Authentication.AuthEntity.AuthBusOperatorCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthBusOperatorCompanyRepository extends JpaRepository<AuthBusOperatorCompany,String> {
        Optional<AuthBusOperatorCompany> findByBusinessEmail(String email);
    Optional<AuthBusOperatorCompany>findByRegistrationNumberOrBusinessEmail(String registrationNumber,String email);
    Optional<AuthBusOperatorCompany>findByRegistrationNumber(String registrationNumber);
    Boolean existsByBusinessEmail(String email);
    Boolean existsByRegistrationNumber(String registrationNumber);

    Boolean existsByRegistrationNumberOrBusinessEmail(String username,String email);
}
