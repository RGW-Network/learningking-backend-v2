package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.CourseTopic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseTopicResponseDTO extends CourseTopic {
    private Float progress;
    private Boolean isPreview;
    private List<CourseLecture> subTopics=new ArrayList<>();
}
