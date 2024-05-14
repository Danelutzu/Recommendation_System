package com.example.backend.services;

import com.example.backend.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.backend.dtos.UserDTO;
import com.example.backend.dtos.builders.UserBuilder;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(UserBuilder::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(int id){
        Optional<User> userOptional=userRepository.findById(id);
        if (!userOptional.isPresent()){
            LOGGER.error("User with id {} was not found",id);
            throw new ResourceNotFoundException(User.class.getSimpleName()+" with id: "+id);
        }
        return UserBuilder.toDTO(userOptional.get());
    }

    public UserDTO getUserByUsername(String username){
        Optional<User> userOptional= userRepository.findByUsername(username);
        if (!userOptional.isPresent()){
            throw new ResourceNotFoundException(User.class.getSimpleName()+" with username: "+username);
        }
        return UserBuilder.toDTO(userOptional.get());
    }

    public int insert(UserDTO userDTO){
        User user = UserBuilder.toEntity(userDTO);
        user=userRepository.save(user);
        LOGGER.debug("User with id {} was inserted",user.getId());
        return user.getId();
    }

    public int delete(UserDTO userDTO){
        User user = UserBuilder.toEntity(userDTO);
        int idDeleted = user.getId();
        userRepository.delete(user);
        LOGGER.debug("User with id {} was deleted",idDeleted);
        return idDeleted;
    }

    public int update(UserDTO userDTOInitial, UserDTO userDTOUpdated){
        User user = UserBuilder.toEntity(userDTOUpdated);
        int idUpdate = user.getId();
        userDTOInitial.setUsername(userDTOUpdated.getUsername());
        userDTOInitial.setPassword(userDTOUpdated.getPassword());
        userDTOInitial.setEmail(userDTOUpdated.getEmail());

        User user1 = UserBuilder.toEntity(userDTOInitial);
        userRepository.save(user1);
//        delete(userDTOInitial);
//        insert(userDTOUpdated);
        LOGGER.debug("User with id {} was updated",idUpdate);
        return idUpdate;
    }

    public User logIn(UserDTO userDTO){
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        if (!optionalUser.isPresent()){
            throw new ResourceNotFoundException("There is no user with this username");
        }

        if (!optionalUser.get().getPassword().equals(userDTO.getPassword())){
            throw new ResourceNotFoundException("There is no user with these log in credentials");
        }
        return UserBuilder.toEntity(userDTO);
    }


}
