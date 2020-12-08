package com.codecool.binder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Data Transfer Object
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String profilePicture;
    private Map<String, String> profileNames;
    private Set<String> interests;
    private Set<Long> projects;
    private Set<Long> posts;
}
