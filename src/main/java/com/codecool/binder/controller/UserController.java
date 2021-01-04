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
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.updateUser(user, sessionUserEmail);
    }

    //delete user
    @DeleteMapping("{id}")
    public void deleteUser (@PathVariable("id") Long id) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteUser(id, sessionUserEmail);
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
    @PostMapping("match")
    public void match (@RequestBody Long targetId) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.match(targetId, sessionUserEmail);
    }

    //add nope
    @PostMapping("nope")
    public void nope (@RequestBody Long targetId) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.nope(targetId, sessionUserEmail);
    }

    //add banned user
    @PostMapping("ban")
    public void ban (@RequestBody Long targetId) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.ban(targetId, sessionUserEmail);
    }

    //search by interest
    @GetMapping("search-interest")
    public List<UserDto> searchByInterest (@RequestParam String interest) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getSearchByInterest(interest, sessionUserEmail);
    }

    //search by name
    @GetMapping("search-name")
    public List<UserDto> searchByName(@RequestParam String name) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getSearchByName(name, sessionUserEmail);
    }

}
