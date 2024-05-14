package com.example.backend.dtos.builders;

import com.example.backend.dtos.UserDTO;
import com.example.backend.entities.User;

public class UserBuilder {

    public static UserDTO toDTO(User user){
        return new UserDTO(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole());
    }

    public static User toEntity(UserDTO userDTO){
        return new User(userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getRole());
    }

}
