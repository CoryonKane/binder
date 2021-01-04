package com.codecool.binder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    @Column(nullable = false)
    private User owner;
    @Column(nullable = false)
    private String title;
    private String description;
    private String pictureUrl;
    @Column(nullable = false)
    private Date date;
}
