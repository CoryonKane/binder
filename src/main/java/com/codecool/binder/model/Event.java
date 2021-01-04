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
    private Date date;
    @ManyToOne(optional = false)
    private User owner;
    @ManyToMany
    private Set<User> participants = new HashSet<>();
}
