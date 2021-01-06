package com.codecool.binder.service;

import com.codecool.binder.dto.ProjectDto;
import com.codecool.binder.model.Project;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository repository;
    private final UserService userService;

    @Autowired
    public ProjectService(ProjectRepository repository, @Lazy UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public ProjectDto convert (Project p) {
        return ProjectDto.builder()
                .id(p.getId())
                .url(p.getUrl())
                .title(p.getTitle())
                .owner(p.getOwner().getId())
                .description(p.getDescription())
                .build();
    }

    public ProjectDto getProject (Long id) {
        Project p = repository.getOne(id);
        return convert(p);
    }

    public ProjectDto createProject (Project project, String sessionUserEmail) {
        User sessionUser = userService.getUserByEmail(sessionUserEmail);
        project.setId(null);
        project.setOwner(sessionUser);
        repository.save(project);
        return convert(repository.getOne(project.getId()));
    }

    public ProjectDto updateProject (Project project, String sessionUserEmail) {
        User user = userService.getUserByEmail(sessionUserEmail);
        if (repository.getOne(project.getId()).getOwner().equals(user)) {
            repository.save(project);
            return convert(repository.getOne(project.getId()));
        } else throw new BadCredentialsException("Access denied.");
    }

    public void deleteProject (Long id, String sessionUserName) {
        User user = userService.getUserByEmail(sessionUserName);
        Project p = repository.getOne(id);
        if (p.getOwner().getId().equals(user.getId())) {
            repository.deleteById(id);
        } else throw new BadCredentialsException("Access denied.");
    }
}
