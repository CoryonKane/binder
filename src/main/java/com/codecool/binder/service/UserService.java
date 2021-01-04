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
        u.getProjects().forEach(p -> {if (p.isVisible() || isVisible) {projects.add(p.getId());}});
        u.getProfileNames().forEach(p -> {if (p.isVisible() || isVisible) {profiles.add(p.getId());}});
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
        return convert(user, sessionUser.isMatched(user));
    }

    public User getUserByEmail(String sessionUserEmail) {
        return repository.findByEmail(sessionUserEmail).orElse(null);
    }

    public UserDto saveUser(User user, boolean isVisible) {
        repository.save(user);
        return getUserDtoByEmail(user.getEmail(), isVisible);
    }

    public void deleteUser(Long id, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        if (sessionUser.getId().equals(id)) {
            repository.deleteById(id);
        } else throw new BadCredentialsException("Invalid user.");
    }

    public UserDto registerUser (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (repository.findByEmail(user.getEmail()).isEmpty()) {
            user.setId(null);
             return saveUser(user, true);
        } else {
            return null;
        }
    }

    public UserDto updateUser(User user, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        if (sessionUser.getId().equals(user.getId())) {
            return saveUser(user, true);
        } else throw new BadCredentialsException("Invalid user.");
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

    public UserDto getUserDtoByEmail(String dataEmail, boolean isVisible) {
        return convert(repository.findByEmail(dataEmail).orElseThrow(() -> new UsernameNotFoundException(dataEmail)), isVisible);
    }

    public void match(Long targetUserId, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        User target = repository.getOne(targetUserId);
        if (sessionUser.equals(target) ||
                sessionUser.isMatched(target) ||
                sessionUser.isFollowed(target) ||
                sessionUser.isBanned(target)) {
            return;
        }
        if (sessionUser.isNoped(target)) {
            sessionUser.removeNope(target);
        }
        if (target.isFollowed(sessionUser)) {
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
        User sessionUser = getUserByEmail(sessionUserEmail);
        User target = repository.getOne(targetId);
        if (sessionUser.equals(target) ||
                sessionUser.isNoped(target) ||
                sessionUser.isBanned(target)) {
            return;
        }
        if (sessionUser.isMatched(target)) {
            sessionUser.removeMatch(target);
            target.removeMatch(sessionUser);
            target.addFollow(sessionUser);
        }
        if (sessionUser.isFollowed(target)) {
            sessionUser.removeFollow(target);
        }
        sessionUser.addNope(target);
        repository.save(sessionUser);
    }

    public List<UserDto> getSearchByInterest(String interest, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        List<String> searches = Arrays.stream(interest.split(",")).map(String::trim).collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        searches.forEach(s -> users.addAll(repository.findByInterestsContaining(s)));
        return users.stream()
                .filter(user -> !user.isNoped(sessionUser))
                .filter(u -> !u.isBanned(sessionUser))
                .distinct()
                .map(user -> convert(user, sessionUser.isMatched(user)))
                .collect(Collectors.toList());
    }

    public List<UserDto> getSearchByName(String name, String sessionUserEmail) {
        User sessionUser = getUserByEmail(sessionUserEmail);
        List<String> names = Arrays.stream(name.split(",")).map(String::trim).collect(Collectors.toList());
        return repository.findByLastNameIsInOrFirstNameIsIn(names, names)
                .stream()
                .filter(u -> !u.isBanned(sessionUser))
                .distinct()
                .map(u -> convert(u, sessionUser.isMatched(u)))
                .collect(Collectors.toList());
    }
}
