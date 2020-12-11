package com.codecool.binder.controller;

import com.codecool.binder.dto.UserDto;
import com.codecool.binder.model.UserPassword;
import com.codecool.binder.model.User;
import com.codecool.binder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public UserDto getUser (@PathVariable("id") Long id, Principal principal) {
        User sessionUser = (User) principal;
        return service.getUserDto(id, sessionUser);
    }

    //update user info
    @PutMapping("")
    public UserDto updateUser (@RequestBody User user) {
        return service.saveUser(user, true);
    }

    //delete user
    @DeleteMapping("{id}")
    public void deleteUser (@PathVariable("id") Long id) {
        service.deleteUser(id);
    }

    //get sessionUser's lists
    @GetMapping("lists")
    public Map<String, List<Long>> getLists (Principal principal) {
        User sessionUser = (User) principal;
        return service.getLists(sessionUser);
    }

    //change user password
    @PutMapping("change-password")
    public void changeUserPassword (@RequestBody UserPassword userPassword, Principal principal) {
        User sessionUser = (User) principal;
        service.changeUserPassword(sessionUser, userPassword);
    }

    //add match
    @PostMapping("match/{id}")
    public void match (@PathVariable("id") Long targetUserId, Principal principal) {
        User sessionUser = (User) principal;
        service.match(targetUserId, sessionUser);
    }

    //search by interest
    @GetMapping("search-interest")
    public List<UserDto> searchByInterest (@RequestParam String search, Principal principal) {
        User sessionUser = (User) principal;
        return service.getSearchByInterest(search, sessionUser);
    }

    //search by name
    @GetMapping("search-name")
    public List<UserDto> searchByUsername (@RequestParam String name, Principal principal) {
        User sessionUser = (User) principal;
        return service.getSearchByUsername(name, sessionUser);
    }
}
