package com.byaffe.learningking.dtos.auth;

import lombok.Data;

@Data
public class UserEmailVerificationRequestDTO {
    public String emailAddress;
    public String otp;



}
