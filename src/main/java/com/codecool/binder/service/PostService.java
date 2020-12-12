package com.codecool.binder.service;

import com.codecool.binder.dto.PostDto;
import com.codecool.binder.model.Post;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {
    private final PostRepository repository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository repository, @Lazy UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public PostDto convert (Post p) {
        return PostDto.builder()
                .id(p.getId())
                .description(p.getDescription())
                .owner(p.getOwner().getId())
                .pictureUrl(p.getPictureUrl())
                .title(p.getTitle())
                .date(p.getDate())
                .build();
    }

    public PostDto getProject(Long id) {
        Post p = repository.getOne(id);
        return convert(p);
    }

    public PostDto savePost(Post post, String sessionUserEmail) {
        User sessionUser = userService.getUserByEmail(sessionUserEmail);
        if (post.getDate() != null) {
            post.setDate(new Date());
        }
        post.setOwner(sessionUser);
        repository.save(post);
        return convert(post);
    }

    public void deletePost(Long id) {
        repository.deleteById(id);
    }

    public List<PostDto> getNews(String sessionUserEmail) {
        User sessionUser = userService.getUserByEmail(sessionUserEmail);
        List<User> users = Stream.concat(sessionUser.getFollowList().stream(), sessionUser.getMatchList().stream())
                .collect(Collectors.toList());
        return repository.findByOwnerIsIn(users)
                .stream()
                .map(this::convert)
                .sorted(Comparator.comparing(PostDto::getDate))
                .collect(Collectors.toList());
    }
}
