package com.codecool.binder.service;

import com.codecool.binder.dto.PostDto;
import com.codecool.binder.dto.UserDto;
import com.codecool.binder.model.UserPassword;
import com.codecool.binder.model.Post;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository) {
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.repository = repository;
    }

    public UserDto convert (User u, boolean isVisible) {
        Set<Long> projects = new HashSet<>();
        Set<Long> profiles = new HashSet<>();
        u.getProjects().forEach((k, v) -> {if (v || isVisible) {projects.add(k.getId());}});
        u.getProfileNames().forEach((k, v) -> {if (v || isVisible) {profiles.add(k.getId());}});
        return UserDto.builder()
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .id(u.getId())
                .interests(u.getInterests())
                .nickName(u.getNickName())
                .profilePicture(u.getProfilePicture())
                .profiles(profiles)
                .posts(u.getPosts().stream().map(Post::getId).collect(Collectors.toSet()))
                .projects(projects)
                .build();
    }

    public UserDto getUserDto(Long id, User sessionUser) {
        User user = repository.getOne(id);
        boolean isVisible = sessionUser.getFollowList().contains(user) || sessionUser.getMatchList().contains(user);
        return convert(user, isVisible);
    }

    public UserDto saveUser(User user, boolean isVisible) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return convert(user, isVisible);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
    
    public UserDto registerUser (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return saveUser(user, true);
    }

    public void changeUserPassword(User sessionUser, UserPassword userPassword) {
        if (sessionUser.getPassword().equals(passwordEncoder.encode(userPassword.getOldPassword()))) {
            sessionUser.setPassword(passwordEncoder.encode(userPassword.getNewPassword()));
            repository.save(sessionUser);
        } else throw new BadCredentialsException("Wrong username or password.");
    }

    public Map<String, List<Long>> getLists(User sessionUser) {
        Map<String, List<Long>> listMap = new HashMap<>();
        listMap.put("nopeList", sessionUser.getNopeList().stream().map(User::getId).collect(Collectors.toList()));
        listMap.put("matchList", sessionUser.getMatchList().stream().map(User::getId).collect(Collectors.toList()));
        listMap.put("followList", sessionUser.getFollowList().stream().map(User::getId).collect(Collectors.toList()));
        return listMap;
    }
    
    public UserDto getUserDtoByEmail(String dataEmail) {
        return convert(repository.findByEmail(dataEmail).orElseThrow(() -> new UsernameNotFoundException(dataEmail)), true);
    }

    public void match(Long targetUserId, User sessionUser) {
        User target = repository.getOne(targetUserId);
        if (target.hasMatch(sessionUser)) {
            target.removeFollow(sessionUser);
            target.addMatch(sessionUser);
            sessionUser.addMatch(target);
        } else {
            sessionUser.addFollow(target);
        }
    }
}
