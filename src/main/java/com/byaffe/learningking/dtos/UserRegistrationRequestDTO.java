package com.byaffe.learningking.dtos;

import lombok.Data;

@Data
public class UserRegistrationRequestDTO {
    public String emailAddress;
    public String lastName;
    public String firstName;
    public String password;
    public String confirmPassword;
    public Long countryId;



}
