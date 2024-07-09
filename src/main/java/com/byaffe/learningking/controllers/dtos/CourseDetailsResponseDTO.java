package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseSubscription;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDetailsResponseDTO {


    private CourseResponseDTO course;
    private CourseSubscription subscription;


}
