package com.codecool.binder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Project {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String url;
    private String title;
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", updatable = false)
    private User owner;
}
