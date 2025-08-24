package com.byaffe.learningking.dtos.instructor;

import com.byaffe.learningking.shared.constants.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InstructorRequestDTO {
    private Long id;
    private String emailAddress;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private Long countryId;
    @JsonIgnore
    private MultipartFile coverImage;
    @JsonIgnore
    private MultipartFile profileImage;
    private String designation;
    private String imageUrl;
    private String biography;
    private Gender gender;
}
