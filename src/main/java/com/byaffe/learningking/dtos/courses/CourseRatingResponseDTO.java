package com.byaffe.learningking.dtos.courses;


import lombok.Data;

@Data
public class CourseRatingResponseDTO  {

    private Long courseId;
    private String ratingText;
    private String dateCreated;
    private String studentFullName;
    private Double stars;



  
}
