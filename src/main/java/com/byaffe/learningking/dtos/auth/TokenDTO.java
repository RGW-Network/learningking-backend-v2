package com.byaffe.learningking.dtos.auth;

import lombok.Data;

@Data
public class TokenDTO {
    private String username;
    private String password;
    private boolean rememberMe;


}
