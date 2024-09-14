package com.byaffe.learningking.dtos.instructor;

import java.util.Set;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String lastName;
    private String firstName;
      private String password;
    private String phoneNumber;
    private String countryName;
    private String imageUrl;
    private Set<String> interests;
    private String newPassword;
    private String soulId;
     private String imageBase64;
      private String code;


}
