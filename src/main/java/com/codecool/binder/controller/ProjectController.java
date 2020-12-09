package com.codecool.binder.controller;

import com.codecool.binder.dto.ProjectDto;
import com.codecool.binder.model.Project;
import com.codecool.binder.model.User;
import com.codecool.binder.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return service.getProject(id);
    }

    @PostMapping("")
    public ProjectDto createProject (@RequestBody Project project, Principal principal) {
        User sessionUser = (User) principal;
        return service.saveProject(project, sessionUser);
    }

    @PutMapping("")
    public ProjectDto updateProject (@RequestBody Project project, Principal principal) {
        User sessionUser = (User) principal;
        return service.saveProject(project, sessionUser);
    }

    @DeleteMapping("{id}")
    public void deleteProject (@PathVariable("id") Long id) {
        service.deleteProject(id);
    }
}
