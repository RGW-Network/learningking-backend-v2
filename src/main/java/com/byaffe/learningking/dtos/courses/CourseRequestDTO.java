package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.OwnershipType;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CourseCategoryService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
public class CourseRequestDTO {
    private Long id;
    private String title;
    private String description;
    private int numberOfTopics = 0;
    private MultipartFile coverImage;
    private String welcomeVideoUrl;
    private String whatYouWillLearn;
    private List<String> tags;
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    private String certificateTemplate;
    private Long categoryId;
    private int ownershipTypeId ;
    private long company;
    private int academyId;
    private boolean isFeatured;
    private boolean isPaid;
    private float cost;
    private String fullDescription;

  private CourseAcademyType academy=CourseAcademyType.getById(academyId);

  @JsonIgnore
    public CourseOwnerShipType getOwnershipType(){
        return CourseOwnerShipType.getById(this.ownershipTypeId);
    }
    @JsonIgnore
    public CourseAcademyType getAcademy(){
        return CourseAcademyType.getById(this.academyId);
    }


}
