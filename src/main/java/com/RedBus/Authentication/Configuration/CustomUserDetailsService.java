package com.RedBus.Authentication.Configuration;

import com.RedBus.Authentication.AuthEntity.AuthBusOperatorCompany;
import com.RedBus.Authentication.AuthEntity.AuthUsers;
import com.RedBus.Authentication.AuthEntity.Role;
import com.RedBus.Authentication.repository.AuthBusOperatorCompanyRepository;
import com.RedBus.Authentication.repository.AuthUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUsersRepository userRepository;
    private final AuthBusOperatorCompanyRepository authBusOperatorCompanyRepository;

    @Autowired
    public CustomUserDetailsService(AuthUsersRepository userRepository,AuthBusOperatorCompanyRepository authBusOperatorCompanyRepository) {
        this.userRepository = userRepository;
        this.authBusOperatorCompanyRepository=authBusOperatorCompanyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws
            UsernameNotFoundException {
        AuthUsers user = userRepository.findByUserNameOrEmail(usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));


        if(user!=null) {
            return new
                    org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        }else {
            AuthBusOperatorCompany operator = authBusOperatorCompanyRepository.findByRegistrationNumberOrBusinessEmail(usernameOrEmail,usernameOrEmail).orElseThrow(()->new UsernameNotFoundException("Operator company not found with registration number or email"+usernameOrEmail));

            return new
                org.springframework.security.core.userdetails.User(operator.getRegistrationNumber(),
                operator.getPassword(), mapRolesToAuthorities(operator.getRoles()));}
    }
    private Collection< ? extends GrantedAuthority>
    mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new
                SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
