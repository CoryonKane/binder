package com.codecool.binder.controller;

import com.codecool.binder.dto.ProfileDto;
import com.codecool.binder.model.Profile;
import com.codecool.binder.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profile/")
public class ProfileController {
    private final ProfileService service;

    @Autowired
    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ProfileDto getProfile (@PathVariable("id") Long id) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getProfileDto(id, sessionUserEmail);
    }

    @PostMapping("")
    public ProfileDto createProfiles (@RequestBody Profile profile) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.createProfile(profile, sessionUserEmail);
    }

    @PutMapping("")
    public ProfileDto updateProfiles (@RequestBody Profile profile) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.updateProfile(profile, sessionUserEmail);
    }

    @DeleteMapping("{id}")
    public void deleteProfile (@PathVariable("id") Long id) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteProfile(id, sessionUserEmail);
    }
}
