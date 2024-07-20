package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.OwnershipType;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CourseCategoryService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String coverImageUrl;
    private MultipartFile coverImage;
    private String welcomeVideoUrl;
    private String whatYouWillLearn;
    private List<String> tags;
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    private String certificateTemplate;
    private Long categoryId;
    private CourseOwnerShipType ownershipType ;
    private long company;
    private Long instructorId;
    private int academyId;
    private boolean isFeatured;
    private boolean isPaid;
    private float price;
    private float discountedPrice;
    private String fullDescription;
  private CourseAcademyType academy;


}
