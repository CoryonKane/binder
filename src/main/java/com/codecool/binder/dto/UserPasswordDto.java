package com.codecool.binder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordDto {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
