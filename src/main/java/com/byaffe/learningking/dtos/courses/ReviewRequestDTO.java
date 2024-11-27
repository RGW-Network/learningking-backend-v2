package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.courses.ReviewType;
import lombok.Data;

@Data
public class ReviewRequestDTO {
    private String comment;
    private Double starsCount;//out of 5
    private ReviewType type=ReviewType.GENERAL;
    private Long recordId;
    private String reviewerName;
    private String reviewerDesignation;
    private String reviewerImageUrl;




  
}
