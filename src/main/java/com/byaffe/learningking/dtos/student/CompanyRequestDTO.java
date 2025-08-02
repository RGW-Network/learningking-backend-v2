package com.byaffe.learningking.dtos.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CompanyRequestDTO  {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String mobileNumber;
    private String telephoneNumber;
    private Long countryId;
    private Long areaOfBusinessId;
    private String emailAddress;
    private String coverImageUrl;
    private String logoImageUrl;

    @JsonIgnore
    private MultipartFile coverImage;

    @JsonIgnore
    private MultipartFile logoImage;



}
