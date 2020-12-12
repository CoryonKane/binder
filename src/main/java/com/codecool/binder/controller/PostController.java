package com.codecool.binder.controller;

import com.codecool.binder.dto.PostDto;
import com.codecool.binder.model.Post;
import com.codecool.binder.model.User;
import com.codecool.binder.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("post/")
public class PostController {
    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public PostDto getPost (@PathVariable("id") Long id) {
        return service.getProject(id);
    }

    @PostMapping("")
    public PostDto createPost (@RequestBody Post post) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.savePost(post, sessionUserEmail);
    }

    @PutMapping("")
    public PostDto updatePost(@RequestBody Post post) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.savePost(post, sessionUserEmail);
    }

    @DeleteMapping("{id}")
    public void deletePost (@PathVariable("id") Long id) {
        service.deletePost(id);
    }

    @GetMapping("news")
    public List<PostDto> getNews () {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getNews(sessionUserEmail);
    }
}
