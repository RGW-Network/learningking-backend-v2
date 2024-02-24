package com.byaffe.microtasks.dtos;

import lombok.Data;

@Data
public class UserEmailVerificationRequestDTO {
    public String emailAddress;
    public String otp;



}
