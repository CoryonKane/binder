package com.codecool.binder.service;

import com.codecool.binder.dto.EventDto;
import com.codecool.binder.model.Event;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
                .date(e.getDate())
                .owner(e.getOwner().getId())
                .participants(e.getParticipants().stream().map(User::getId).collect(Collectors.toList()))
                .build();
    }

    public EventDto getEventById(Long id) {
        return convert(repository.getOne(id));
    }

    public EventDto saveEvent(Event event) {
        repository.save(event);
        return convert(event);
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }

    public List<EventDto> searchEvent(String search, String sessionUserEmail) {
        User sessionUser = userService.getUserByEmail(sessionUserEmail);
        List<String> searches = Arrays.stream(search.split(",")).map(String::trim).collect(Collectors.toList());
        return repository.findByTitleIsIn(searches)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
