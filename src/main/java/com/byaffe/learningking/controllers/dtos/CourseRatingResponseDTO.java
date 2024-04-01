package com.byaffe.learningking.controllers.dtos;


import lombok.Data;

@Data
public class CourseRatingResponseDTO  {

    private Long courseId;
    private String ratingText;
    private String dateCreated;
    private String memberFullName;
    private float stars;



  
}
