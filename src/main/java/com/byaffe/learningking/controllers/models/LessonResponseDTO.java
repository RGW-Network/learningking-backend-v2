package com.byaffe.learningking.controllers.models;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.Testimonial;
import com.byaffe.learningking.services.CourseLessonService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LessonResponseDTO extends CourseLesson {

  private Float progress;
                   private Boolean isPreview;
                   private List<CourseTopicResponseDTO> topics= new ArrayList<>();

}
