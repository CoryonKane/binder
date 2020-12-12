package com.codecool.binder.controller;

import com.codecool.binder.dto.UserDto;
import com.codecool.binder.model.UserPassword;
import com.codecool.binder.model.User;
import com.codecool.binder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getUserDto(id, sessionUserEmail);
    }

    //update user info
    @PutMapping("")
    public UserDto updateUser (@RequestBody User user) {
        return service.registerUser(user);
    }

    //delete user
    @DeleteMapping("{id}")
    public void deleteUser (@PathVariable("id") Long id) {
        service.deleteUser(id);
    }

    //get sessionUser's lists
    @GetMapping("lists")
    public Map<String, List<Long>> getLists () {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getLists(sessionUserEmail);
    }

    //change user password
    @PutMapping("change-password")
    public void changeUserPassword (@RequestBody UserPassword userPassword) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.changeUserPassword(sessionUserEmail, userPassword);
    }

    //add match
    @PostMapping("match/{id}")
    public void match (@PathVariable("id") Long targetUserId) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.match(targetUserId, sessionUserEmail);
    }

    //search by interest
    @GetMapping("search-interest")
    public List<UserDto> searchByInterest (@RequestParam String search) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getSearchByInterest(search, sessionUserEmail);
    }

    //search by name
    @GetMapping("search-name")
    public List<UserDto> searchByUsername (@RequestParam String name) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getSearchByUsername(name, sessionUserEmail);
    }
}
