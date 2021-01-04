package com.codecool.binder.controller;

import com.codecool.binder.dto.EventDto;
import com.codecool.binder.model.Event;
import com.codecool.binder.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("event/")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public EventDto getEvent (@PathVariable("id") Long id) {
        return service.getEventById(id);
    }

    @PostMapping("")
    public EventDto createEvent (@RequestBody Event event) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.createEvent(event, sessionUserEmail);
    }

    @PutMapping("")
    public EventDto updateEvent (@RequestBody Event event) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.createEvent(event, sessionUserEmail);
    }

    @DeleteMapping("{id}")
    public void deleteEvent (@PathVariable("id") Long id) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteEvent(id, sessionUserEmail);
    }

    @GetMapping("search")
    public List<EventDto> searchEvents (@RequestParam String eventTitle) {
        return service.searchEvent(eventTitle);
    }
}
