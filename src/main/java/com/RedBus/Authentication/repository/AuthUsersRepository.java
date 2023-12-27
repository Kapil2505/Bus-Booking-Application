package com.RedBus.Authentication.repository;

import com.RedBus.Authentication.AuthEntity.AuthUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUsersRepository extends JpaRepository<AuthUsers,String> {
    Optional<AuthUsers> findByEmail(String email);
    Optional<AuthUsers>findByUserNameOrEmail(String username,String email);
    Optional<AuthUsers>findByUserName(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String username);

    Boolean existsByUserNameOrEmail(String username,String email);
}
