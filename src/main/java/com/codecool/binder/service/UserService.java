package com.codecool.binder.service;

import com.codecool.binder.dto.UserDto;
import com.codecool.binder.dto.UserPasswordDto;
import com.codecool.binder.model.Post;
import com.codecool.binder.model.Project;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // TODO Project dto-kat be lehet ide rakni ha máshol nem kérjük le.
    public UserDto convert (User u) {
        return UserDto.builder()
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .id(u.getId())
                .interests(u.getInterests())
                .nickName(u.getNickName())
                .profilePicture(u.getProfilePicture())
                .profileNames(u.getProfileNames())
                .posts(u.getPosts().stream().map(Post::getId).collect(Collectors.toSet()))
                .projects(u.getProjects().stream().map(Project::getId).collect(Collectors.toSet()))
                .build();
    }

    public UserDto getUserDto(Long id) {
        User u = repository.getOne(id);
        return convert(u);
    }

    public UserDto saveUser(User user) {
        repository.save(user);
        return convert(user);
    }

    // TODO
    public void changeUserPassword(UserPasswordDto userPasswordDto) {
    }
}
