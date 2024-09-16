package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CourseTopicRequestDTO {
    private Long id= 0L;
    private String title;
    private String description;
    private int position = 1;
    private PublicationStatus publicationStatus;
    private Long courseLessonId;

    private MultipartFile coverImage;


}
