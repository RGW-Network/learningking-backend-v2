package com.byaffe.learningking.controllers.models;

import com.byaffe.learningking.models.Subscription;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseSubscription;
import com.byaffe.learningking.models.courses.Testimonial;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDetailsResponseDTO {


    private CourseResponseDTO course;
    private CourseSubscription subscription;
    private List<CourseLesson> lessons=new ArrayList<>();
    private Integer numberOfLessons;

}
