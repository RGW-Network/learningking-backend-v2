package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.Testimonial;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CourseResponseDTO extends Course {


    private boolean enrolled;
    private Set<Testimonial> testimonials =new HashSet<>();
    private List<LessonResponseDTO> lessons =new ArrayList<>();
    private int numberOfLessons;
    private int numberOfTopics;
private int daysToEndOfDiscount;
    private double averageRating;
    private int ratingsCount;
    private Float progress;

}
