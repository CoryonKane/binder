package com.codecool.binder.service;

import com.codecool.binder.dto.PostDto;
import com.codecool.binder.model.Post;
import com.codecool.binder.model.Project;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public PostDto convert (Post p) {
        return PostDto.builder()
                .id(p.getId())
                .description(p.getDescription())
                .owner(p.getOwner().getId())
                .pictureUrl(p.getPictureUrl())
                .title(p.getTitle())
                .build();
    }

    public PostDto getProject(Long id) {
        Post p = repository.getOne(id);
        return convert(p);
    }

    public PostDto savePost(Post post, User sessionUser) {
        post.setOwner(sessionUser);
        repository.save(post);
        return convert(post);
    }

    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
