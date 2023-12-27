package com.RedBus.Authentication.AuthController;

import com.RedBus.Authentication.AuthEntity.AuthBusOperatorCompany;
import com.RedBus.Authentication.AuthEntity.AuthUsers;
import com.RedBus.Authentication.AuthEntity.Role;
import com.RedBus.Authentication.Configuration.JwtTokenProvider;
import com.RedBus.Authentication.Payload.*;
import com.RedBus.Authentication.repository.AuthBusOperatorCompanyRepository;
import com.RedBus.Authentication.repository.AuthUsersRepository;
import com.RedBus.Authentication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthBusOperatorCompanyRepository operatorRepository;
    private AuthUsersRepository authUsersRepository;

    private PasswordEncoder getEncode;
    private AuthenticationManager authenticationManager;

    private RoleRepository roleRepository;

    private JwtTokenProvider tokenProvider;

    public AuthController(AuthBusOperatorCompanyRepository operatorRepository, AuthUsersRepository authUsersRepository,PasswordEncoder getEncode,AuthenticationManager authenticationManager,RoleRepository roleRepository,JwtTokenProvider tokenProvider) {
        this.operatorRepository = operatorRepository;
        this.authUsersRepository = authUsersRepository;
        this.getEncode=getEncode;
        this.authenticationManager=authenticationManager;
        this.roleRepository=roleRepository;
        this.tokenProvider=tokenProvider;
    }

    @PostMapping("/signUp/OperatorCompany")
    public ResponseEntity<?> signUpOperatorCompany(@RequestBody SignUpDtoForOperator operator)
    {
        if(operatorRepository.existsByRegistrationNumber(operator.getRegistrationNumber()))
        {
            return new ResponseEntity<>("Registration Number already exist ! "+operator.getRegistrationNumber(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(operatorRepository.existsByBusinessEmail(operator.getBusinessEmail()))
        {
            return new ResponseEntity<>("Business Email already exist ! "+operator.getBusinessEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        AuthBusOperatorCompany operatorCompany = new AuthBusOperatorCompany();
        String id = UUID.randomUUID().toString();
        operatorCompany.setOperatorId(id);
        operatorCompany.setCompanyName(operator.getCompanyName());
        operatorCompany.setCompanyAddress(operator.getCompanyAddress());
        operatorCompany.setRegistrationNumber(operator.getRegistrationNumber());
        operatorCompany.setBusinessEmail(operator.getBusinessEmail());
        operatorCompany.setBusinessPhone(operator.getBusinessPhone());
        operatorCompany.setPassword(getEncode.encode(operator.getPassword()));
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").get();
        operatorCompany.setRoles(Collections.singleton(roleAdmin));
        operatorRepository.save(operatorCompany);

        return new ResponseEntity<>(" Operator Company SignUp successFully !",HttpStatus.OK);

    }

    @PostMapping("/signUp/User")
    public ResponseEntity<?>signUpUser(@RequestBody SignUpDtoForUser signupDto)
    {
        if(authUsersRepository.existsByEmail(signupDto.getEmail()))
        {
            return new ResponseEntity<>("email exist already !"+signupDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(authUsersRepository.existsByEmail(signupDto.getUserName()))
        {
            return new ResponseEntity<>("UserName exist already !"+signupDto.getUserName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        AuthUsers user = new AuthUsers();
        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        user.setFullName(signupDto.getFullName());
        user.setDateOfBirth(signupDto.getDateOfBirth());
        user.setEmail(signupDto.getEmail());
        user.setMobile(signupDto.getMobile());
        user.setUserName(signupDto.getUserName());
        user.setPassword(getEncode.encode(signupDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(role));
        authUsersRepository.save(user);
        return new ResponseEntity<>("user Registered !",HttpStatus.CREATED);
    }


    @PostMapping("/SignIn/OperatorCompany")
    public ResponseEntity<JWTAuthResponse>authenticateCompanyOperator(@RequestBody LoginDtoForCompany loginDto)
    {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getRegistrationNumberOrBusinessEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenProvider.generateToken(authenticate);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }
    @PostMapping("/SignIn/User")
    public ResponseEntity<JWTAuthResponse>authenticateUser(@RequestBody LoginDtoUser loginDto)
    {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenProvider.generateToken(authenticate);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }
}
