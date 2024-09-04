package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.api.BaseDTO;
import com.byaffe.learningking.shared.models.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CompanyRequestDTO  {
    public Long id;
    public String name;
    public String description;
    private String website;
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
