package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.Testimonial;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseResponseDTO extends Course {


    private boolean enrolled;
    private List<Testimonial> testimonials =new ArrayList<>();
    private List<CourseLesson> lessons =new ArrayList<>();
    private int numberOfLessons;
    private double averageRating;
    private int ratingsCount;
    private Float progress;

}
