package com.codecool.binder.controller;

import com.codecool.binder.dto.PostDto;
import com.codecool.binder.model.Post;
import com.codecool.binder.model.User;
import com.codecool.binder.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PostDto createPost (@RequestBody Post post, Principal principal) {
        User sessionUser = (User) principal;
        return service.savePost(post, sessionUser);
    }

    @PutMapping("")
    public PostDto updatePost(@RequestBody Post post, Principal principal) {
        User sessionUser = (User) principal;
        return service.savePost(post, sessionUser);
    }

    @DeleteMapping("{id}")
    public void deletePost (@PathVariable("id") Long id) {
        service.deletePost(id);
    }

    @GetMapping("news")
    public List<PostDto> getNews (Principal principal) {
        User sessionUser = (User) principal;
        return service.getNews(sessionUser);
    }
}
