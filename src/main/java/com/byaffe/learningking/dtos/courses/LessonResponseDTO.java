package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.courses.CourseLesson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LessonResponseDTO extends CourseLesson {
    private Boolean isPreview;
    private List<CourseTopicResponseDTO> topics = new ArrayList<>();
    private CourseEnrollment subscription;

}
