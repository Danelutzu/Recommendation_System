package com.example.backend.controllers;

import com.example.backend.controllers.handlers.exceptions.model.CustomException;
import com.example.backend.dtos.UserDTO;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") int userId){
        UserDTO userDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping(value = "/users/byName/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username){
        UserDTO userDTO = userService.getUserByUsername(username);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> userDTOList = userService.getAllUsers();
//        for(UserDTO userDTO: userDTOList){
//            Link userLink = linkTo(methodOn(UserController.class).getUser(userDTO.getId())).withRel("userDetails");
//            userDTO.add(userLink);
//        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Integer> insertUser(@Valid @RequestBody UserDTO userDTO){
        int userId = userService.insert(userDTO);
        return new ResponseEntity<>(userId,HttpStatus.CREATED);
    }

    @PutMapping(value = "users/update/{id}")
    public ResponseEntity<Integer> updateUser(@PathVariable("id") int userId, @RequestBody UserDTO userDTO){
        UserDTO userDTO1 = userService.getUserById(userId);
        int userID = userService.update(userDTO1,userDTO);
        return new ResponseEntity<>(userID,HttpStatus.OK);
    }

    @DeleteMapping(value = "users/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId){
        UserDTO userDTO = userService.getUserById(userId);
        userService.delete(userDTO);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity logIn(@RequestBody UserDTO userDTO){
        try{
            User user = userService.logIn(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (CustomException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}
