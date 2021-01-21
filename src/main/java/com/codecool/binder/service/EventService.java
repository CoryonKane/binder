package com.codecool.binder.service;

import com.codecool.binder.dto.EventDto;
import com.codecool.binder.model.Event;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository repository;
    private final UserService userService;

    @Autowired
    public EventService(EventRepository repository, @Lazy UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public EventDto convert (Event e) {
        return EventDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .owner(e.getOwner()
                        .getId())
                .participants(e.getParticipants()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    public EventDto getEventById (Long id, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        Event event = repository.getOne(id);
        if (event.getOwner().isMatched(user) || event.isVisible() || event.getOwner().equals(user)) {
            return convert(repository.getOne(id));
        } else throw new BadCredentialsException("User has no access to event.");
    }

    public EventDto createEvent (Event event, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        event.setOwner(user);
        event.setId(null);
        repository.save(event);
        return convert(event);
    }

    public EventDto updateEvent (Event event, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        if (repository.getOne(event.getId()).getOwner().equals(user)) {
            repository.save(event);
            return convert(repository.getOne(event.getId()));
        } else throw new BadCredentialsException("Access denied.");
    }

    public void deleteEvent (Long id, String sessionUserEmail) {
        Event event = repository.getOne(id);
        User user = userService.getUserByEmail(sessionUserEmail);
        if (event.getOwner().equals(user)) {
            repository.deleteById(id);
        } else throw new BadCredentialsException("User not allowed to delete this event.");
    }

    public List<EventDto> searchEvent(String search, String sessionUserEmail) {
        List<String> searches = Arrays.stream(search.split(",")).map(String::trim).collect(Collectors.toList());
        User user = userService.getUserByEmail(sessionUserEmail);
        return repository.findByTitleIsIn(searches)
                .stream()
                .distinct()
                .filter(event -> event.isVisible() || event.getOwner().isMatched(user))
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public void addParticipant (Long eventId, String sessionUserEmail) {
        Event event = repository.getOne(eventId);
        User user = userService.getUserByEmail(sessionUserEmail);
        if (event.isVisible() || event.getOwner().isMatched(user)) {
            event.addParticipant(user);
            repository.save(event);
        } else throw new BadCredentialsException("User is not allowed to participate in this event.");
    }

    public void removeParticipant (Long eventId, String sessionUserEmail) {
        Event event = repository.getOne(eventId);
        User user = userService.getUserByEmail(sessionUserEmail);
        event.removeParticipant(user);
        repository.save(event);
    }
}
