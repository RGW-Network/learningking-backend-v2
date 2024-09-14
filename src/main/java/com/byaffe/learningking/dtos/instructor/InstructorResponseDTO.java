package com.byaffe.learningking.dtos.instructor;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.shared.constants.Gender;
import lombok.Data;

@Data
public class InstructorResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String phoneNumber;
    private AccountStatus accountStatus ;
    private String imageUrl;
    private String coverImageUrl;
    private Gender gender;
    private int numberOfContributions;
    private String initialPassword;
    private String lastVerificationCode;


}
