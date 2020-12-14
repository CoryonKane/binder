package com.codecool.binder.service;

import com.codecool.binder.dto.UserDto;
import com.codecool.binder.model.UserPassword;
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
        List<Long> projects = new ArrayList<>();
        List<Long> profiles = new ArrayList<>();
        u.getProjects().forEach((k, v) -> {if (v || isVisible) {projects.add(k.getId());}});
        u.getProfileNames().forEach((k, v) -> {if (v || isVisible) {profiles.add(k.getId());}});
        return UserDto.builder()
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .id(u.getId())
                .interests(new ArrayList<>(u.getInterests()))
                .nickName(u.getNickName())
                .profilePicture(u.getProfilePicture())
                .profiles(profiles)
                .projects(projects)
                .build();
    }

    public UserDto getUserDto(Long id, String sessionUserEmail) {
        User user = repository.getOne(id);
        User sessionUser = getUserByEmail(sessionUserEmail);
        return convert(user, sessionUser.isMatch(user));
    }

    public User getUserByEmail(String sessionUserEmail) {
        return repository.findByEmail(sessionUserEmail).orElse(null);
    }

    public UserDto saveUser(User user, boolean isVisible) {
        repository.save(user);
        return convert(user, isVisible);
    }

    public void deleteUser(Long id, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        if (sessionUser.getId().equals(id)) {
            repository.deleteById(id);
        } else throw new BadCredentialsException("Invalid user.");
    }
    
    public UserDto registerUser (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return saveUser(user, true);
    }

    public void changeUserPassword(String sessionUserEmail, UserPassword userPassword) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        if (passwordEncoder.matches(userPassword.getOldPassword(), sessionUser.getPassword())) {
            sessionUser.setPassword(passwordEncoder.encode(userPassword.getNewPassword()));
            repository.save(sessionUser);
        } else throw new BadCredentialsException("Wrong username or password.");
    }

    public Map<String, List<Long>> getLists(String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        Map<String, List<Long>> listMap = new HashMap<>();
        listMap.put("nopeList", sessionUser.getNopeList().stream().map(User::getId).collect(Collectors.toList()));
        listMap.put("matchList", sessionUser.getMatchList().stream().map(User::getId).collect(Collectors.toList()));
        listMap.put("followList", sessionUser.getFollowList().stream().map(User::getId).collect(Collectors.toList()));
        return listMap;
    }
    
    public UserDto getUserDtoByEmail(String dataEmail) {
        return convert(repository.findByEmail(dataEmail).orElseThrow(() -> new UsernameNotFoundException(dataEmail)), true);
    }

    public void match(Long targetUserId, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        User target = repository.getOne(targetUserId);
        if (sessionUser.equals(target)) {
            return;
        }
        if (target.hasMatch(sessionUser)) {
            target.removeFollow(sessionUser);
            target.addMatch(sessionUser);
            sessionUser.addMatch(target);
            repository.save(target);
        } else {
            sessionUser.addFollow(target);
        }
        repository.save(sessionUser);
    }

    public void nope(Long targetId, String sessionUserEmail) {
        User user = getUserByEmail(sessionUserEmail);
        User target = repository.getOne(targetId);
        if (!user.equals(target)) {
            user.addNope(target);
            repository.save(user);
        } else throw new BadCredentialsException("Invalid user.");
    }

    public List<UserDto> getSearchByInterest(String search, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        List<String> searches = Arrays.stream(search.split(",")).map(String::trim).collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        searches.forEach(s -> users.addAll(repository.findByInterestsContaining(s)));
        return users.stream()
                .distinct()
                .map(user -> convert(user, sessionUser.isMatch(user)))
                .collect(Collectors.toList());
    }

    public List<UserDto> getSearchByUsername(String name, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        List<String> names = Arrays.stream(name.split(",")).map(String::trim).collect(Collectors.toList());
        return repository.findByLastNameIsInOrFirstNameIsIn(names, names)
                .stream()
                .distinct()
                .map(u -> convert(u, sessionUser.isMatch(u)))
                .collect(Collectors.toList());
    }

    public UserDto updateUser(User user, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        if (sessionUser.getId().equals(user.getId())) {
            return registerUser(user);
        } else throw new BadCredentialsException("Invalid user.");
    }
}
