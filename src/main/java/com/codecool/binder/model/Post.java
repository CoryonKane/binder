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
    @ManyToOne(optional = false)
    @Immutable
    private User owner;
    @Column(nullable = false)
    private String title;
    private String description;
    private String pictureUrl;
    @Column(nullable = false, updatable = false)
    private Date date;
}
