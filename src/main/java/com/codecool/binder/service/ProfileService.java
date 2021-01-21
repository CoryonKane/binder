package com.codecool.binder.service;

import com.codecool.binder.dto.ProfileDto;
import com.codecool.binder.model.Profile;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository repository;
    private final UserService userService;

    @Autowired
    public ProfileService(ProfileRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public ProfileDto convert (Profile profile) {
        return ProfileDto.builder()
                .id(profile.getId())
                .ownerId(profile.getOwner().getId())
                .userName(profile.getUserName())
                .webPage(profile.getWebPage())
                .build();
    }

    public ProfileDto getProfileDto (Long id, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        Profile profile = repository.getOne(id);
        if (profile.getOwner().equals(user) || profile.getOwner().isMatched(user)) {
            return convert(profile);
        } else throw new BadCredentialsException("Access denied.");
    }

    public ProfileDto createProfile (Profile profile, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        profile.setOwner(user);
        profile.setId(null);
        repository.save(profile);
        return convert(repository.getOne(profile.getId()));
    }

    public ProfileDto updateProfile (Profile profile, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        if (repository.getOne(profile.getId()).getOwner().equals(user)) {
            repository.save(profile);
            return convert(repository.getOne(profile.getId()));
        } else throw new BadCredentialsException("Access denied.");
    }

    public void deleteProfile (Long id, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        if (repository.getOne(id).getOwner().equals(user)) {
            repository.deleteById(id);
        } else throw new BadCredentialsException("Access denied.");
    }
}
