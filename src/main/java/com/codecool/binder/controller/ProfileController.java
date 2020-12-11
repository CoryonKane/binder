package com.codecool.binder.controller;

import com.codecool.binder.dto.ProfileDto;
import com.codecool.binder.model.Profile;
import com.codecool.binder.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return service.getProfileDto(id);
    }

    @PostMapping("")
    public ProfileDto createProfiles (@RequestBody Profile profile) {
        return service.saveProfile(profile);
    }

    @PutMapping("")
    public ProfileDto updateProfiles (@RequestBody Profile profile) {
        return service.saveProfile(profile);
    }

    @DeleteMapping("{id}")
    public void deleteProfile (@PathVariable("id") Long id) {
        service.deleteProfile(id);
    }
}
