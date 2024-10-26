package com.byaffe.learningking.dtos.courses;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseRatingResponseDTO  {

    private Long courseId;
    private String ratingText;
    private LocalDateTime dateCreated;
    private String studentFullName;
    private Double stars;
    private Boolean featured;



  
}
