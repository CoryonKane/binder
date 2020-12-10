package com.codecool.binder.controller;

import com.codecool.binder.dto.EventDto;
import com.codecool.binder.model.Event;
import com.codecool.binder.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return service.saveEvent(event);
    }

    @PutMapping("")
    public EventDto updateEvent (@RequestBody Event event) {
        return service.saveEvent(event);
    }

    @DeleteMapping("{id}")
    public void deleteEvent (@PathVariable("id") Long id) {
        service.deleteEvent(id);
    }
}
