package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.*;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
public class CourseRequestDTO {
    private long id;
    private String title;
    private String description;
    private int numberOfTopics = 0;
    private String coverImageUrl;
    private String welcomeVideoUrl;
    private List<String> whatYouWillLearn;
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    private String certificateTemplate;
    private CourseCategory category;
    private int ownershipType ;
    private long Company;
    private int academy;
    private boolean isFeatured;
    private boolean isPaid;
    private float cost;
    private String fullDescription;
  private Set<Testimonial> testimonials;
}
