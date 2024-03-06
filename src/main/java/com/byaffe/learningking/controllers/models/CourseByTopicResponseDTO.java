package com.byaffe.learningking.controllers.models;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.courses.Testimonial;
import lombok.Data;

import java.util.List;

@Data
public class CourseByTopicResponseDTO extends CourseCategory {
    private List<CourseResponseDTO> courses;

}
