package com.RedBus.Authentication.AuthEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="auth_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUsers {

    @Id
    @Column(name="user_id")
    private String userId;
   @Column(name="full_name")
    private String fullName;
   @Column(name="date_of_birth")
   private Date dateOfBirth;
    private String email;
    private String mobile;

    @Column(name="user_name")
    private String userName;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Role> roles;
}
