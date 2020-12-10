package com.codecool.binder.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    private String title;
    private Date date;
    private boolean isPublic;
    @ManyToOne
    private User owner;
    @ManyToMany
    private List<User> participants;
}
