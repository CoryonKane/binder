package com.codecool.binder.controller;

import com.codecool.binder.dto.UserDto;
import com.codecool.binder.dto.UserPasswordDto;
import com.codecool.binder.model.User;
import com.codecool.binder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    // get user by id
    @GetMapping("{id}")
    public UserDto getUser (@PathVariable("id") Long id) {
        return service.getUserDto(id);
    }

    //register user
    @PostMapping("")
    public UserDto createUser (@RequestBody User user) {
        return service.saveUser(user);
    }

    //update user info
    @PutMapping("")
    public UserDto updateUser (@RequestBody User user) {
        return service.saveUser(user);
    }

    //delete user
    @DeleteMapping("{id}")
    public void deleteUser (@PathVariable("id") Long id) {
        service.deleteUser(id);
    }

    //TODO change user password
    @PutMapping("change-password")
    public void changeUserPassword (@RequestBody UserPasswordDto userPasswordDto) {
        service.changeUserPassword(userPasswordDto);
    }
}
