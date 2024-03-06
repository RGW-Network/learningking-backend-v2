package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.models.courses.CourseCategory;
import lombok.Data;

import java.util.List;

@Data
public class CourseByTopicResponseDTO extends CourseCategory {
    private List<CourseResponseDTO> courses;

}
