package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseOwnerShipType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class LessonRequestDTO {
    private Long id= 0L;
    private String title;
    private String description;
    private int position = 1;
    private String coverImageUrl;
    private String videoUrl;
    private String audioUrl;
    private long courseId;
    private MultipartFile coverImage;
    private String fullDescription;
    private PublicationStatus publicationStatus;


}
