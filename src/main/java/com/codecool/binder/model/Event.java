package com.codecool.binder.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private boolean visible;
    @Column(nullable = false)
    private Date date;
    @ManyToOne(optional = false)
    private User owner;
    @ManyToMany
    private Set<User> participants = new HashSet<>();

    public void addParticipant (User user) {
        this.participants.add(user);
    }

    public void removeParticipant (User user) {
        this.participants.remove(user);
    }

    public boolean isParticipant (User user) {
        return this.participants.contains(user);
    }
}
