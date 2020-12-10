package com.codecool.binder.service;

import com.codecool.binder.dto.EventDto;
import com.codecool.binder.model.Event;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public EventDto convert (Event e) {
        return EventDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .date(e.getDate())
                .isPublic(e.isPublic())
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
}
