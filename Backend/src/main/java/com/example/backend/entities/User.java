package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uc_users_username", columnNames = {"username"}),
        @UniqueConstraint(name = "uc_users_email", columnNames = {"email"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Please enter your username")
    private String username;

    @NotBlank(message = "Please enter your password")
    @Size(min = 8, message = "Password is too short. Please use at least 8 characters")
    private String password;

    @NotBlank(message = "Please enter your email")
    @Email(message = "Invalid email",regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
//    ^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.com$

    @Enumerated(EnumType.STRING)
    private Role role;
}
