package com.codecool.binder.service;

import com.codecool.binder.dto.ProjectDto;
import com.codecool.binder.model.Project;
import com.codecool.binder.model.User;
import com.codecool.binder.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    public ProjectDto saveProject(Project project, String sessionUserEmail) {
        User sessionUser = userService.getUserByEmail(sessionUserEmail);
        project.setOwner(sessionUser);
        repository.save(project);
        return convert(project);
    }

    public void deleteProject(Long id) {
        repository.deleteById(id);
    }
}
