package com.codecool.binder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private List<Long> profiles;
    private List<String> interests;
    private List<Long> projects;
}
