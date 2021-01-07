package com.codecool.binder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    private String pictureUrl;
    @Column(nullable = false, updatable = false)
    private Date date;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", updatable = false)
    private User owner;
}
