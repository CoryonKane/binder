package com.codecool.binder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Profile {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String webPage;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private boolean visible;
    @ManyToOne
    @Column(nullable = false)
    private User owner;
}
