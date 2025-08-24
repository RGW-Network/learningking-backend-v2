package com.byaffe.learningking.dtos.auth;

import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    private String token;
    private String newPassword;


}
