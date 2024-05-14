package com.example.backend.dtos;

import com.example.backend.entities.Role;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO extends RepresentationModel<UserDTO> {

    private int id;
    private String username;
    private String password;
    private String email;
    private Role role;
}
