package com.codecool.binder.repository;

import com.codecool.binder.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTitleIsIn (List<String> searches);
}
