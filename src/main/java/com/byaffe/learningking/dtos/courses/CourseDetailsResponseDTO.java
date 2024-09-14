package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.CourseSubscription;
import lombok.Data;

@Data
public class CourseDetailsResponseDTO {


    private CourseResponseDTO course;
    private CourseSubscription subscription;


}
