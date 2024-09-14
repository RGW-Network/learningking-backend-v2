package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.CourseEnrollment;
import lombok.Data;

@Data
public class CourseDetailsResponseDTO {


    private CourseResponseDTO course;
    private CourseEnrollment subscription;


}
