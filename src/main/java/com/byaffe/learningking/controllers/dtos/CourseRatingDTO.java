package com.byaffe.learningking.controllers.dtos;

import lombok.Data;

@Data
public class CourseRatingDTO  {

    private Long courseId;
    private String ratingText;
    private Float stars;



  
}
