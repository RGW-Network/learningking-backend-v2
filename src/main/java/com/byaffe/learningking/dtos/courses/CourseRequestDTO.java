package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    @JsonIgnore
    private MultipartFile coverImage;
    private Boolean isAdvertised=false;

    private String welcomeVideoUrl;
    private List<String>  tags= new ArrayList<>();
    public String  getCommaSeparatedTags(){
        if(this.tags==null){
            return null;
        }
       return String.join(",", tags);

    }
    private List<String> whatYouWillLearn= new ArrayList<>();
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    private Long certificateTemplateId;
    private Long categoryId;
    private CourseOwnerShipType ownershipType ;
    private Long instructorId;
    private Integer academyId;
    private Boolean offersCertificate=false;
    private Boolean isFeatured=false;
    private Boolean isPaid=false;
    private Float price;
    private Float discountedPrice;
    private String fullDescription;
  private CourseAcademyType academy;



}
