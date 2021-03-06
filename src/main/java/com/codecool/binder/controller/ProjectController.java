package com.codecool.binder.controller;

import com.codecool.binder.dto.ProjectDto;
import com.codecool.binder.model.Project;
import com.codecool.binder.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("project/")
public class ProjectController {
    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ProjectDto getProject (@PathVariable("id") Long id) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getProject(id, sessionUserEmail);
    }

    @PostMapping("")
    public ProjectDto createProject (@RequestBody Project project, Principal principal) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.createProject(project, sessionUserEmail);
    }

    @PutMapping("")
    public ProjectDto updateProject (@RequestBody Project project, Principal principal) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.updateProject(project, sessionUserEmail);
    }

    @DeleteMapping("{id}")
    public void deleteProject (@PathVariable("id") Long id) {
        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteProject(id, sessionUserEmail);
    }
}
