package com.codecool.binder.repository;

import com.codecool.binder.model.Post;
import com.codecool.binder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOwnerIsIn (List<User> users);
}
