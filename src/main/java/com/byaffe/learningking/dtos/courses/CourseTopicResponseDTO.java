package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.courses.CourseTopic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseTopicResponseDTO extends CourseTopic {
    private Boolean isPreview;
    private CourseEnrollment subscription;
    private List<LectureResponseDTO> lectures=new ArrayList<>();
}
