package com.byaffe.learningking.controllers.dtos;

import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.models.courses.CourseTopic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseTopicResponseDTO extends CourseTopic {
    private Float progress;
    private Boolean isPreview;
    private List<CourseSubTopic> subTopics=new ArrayList<>();
}
