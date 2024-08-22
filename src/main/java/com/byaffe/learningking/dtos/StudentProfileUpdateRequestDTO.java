package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
public class StudentProfileUpdateRequestDTO {
    @JsonIgnore
    private Long studentId;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private Long countryId;
    private String location;
    private String bioInformation;
    private String twitterHandle;
    private String facebookUsername;
    private String website;
    private Long professionId;
    private Set<String> interestNames;

    @JsonIgnore
    private MultipartFile coverImage;
    @JsonIgnore
    private MultipartFile profileImage;



}
