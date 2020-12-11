package com.codecool.binder.service;

import com.codecool.binder.dto.ProfileDto;
import com.codecool.binder.model.Profile;
import com.codecool.binder.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository repository;

    @Autowired
    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    public ProfileDto convert (Profile profile) {
        return ProfileDto.builder()
                .id(profile.getId())
                .ownerId(profile.getOwner().getId())
                .userName(profile.getUserName())
                .webPage(profile.getWebPage())
                .build();
    }

    public ProfileDto saveProfile (Profile profile) {
        repository.save(profile);
        return convert(profile);
    }

    public ProfileDto getProfileDto(Long id) {
        return convert(repository.getOne(id));
    }

    public void deleteProfile(Long id) {
        repository.deleteById(id);
    }
}
