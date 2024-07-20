package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.models.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
