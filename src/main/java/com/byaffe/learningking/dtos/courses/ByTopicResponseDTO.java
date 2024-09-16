package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.Category;
import lombok.Data;

import java.util.List;

@Data
public class ByTopicResponseDTO extends Category {
    private List<CourseResponseDTO> courses;

}
