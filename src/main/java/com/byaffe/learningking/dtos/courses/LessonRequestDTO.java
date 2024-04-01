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
    private String title;
    private String description;
    private int position = 1;
    private boolean isLast = false;
    private boolean isFirst = false;
    private String coverImageUrl;
    private String videoUrl;
    private String audioUrl;
    private int publicationStatusId;
    private long courseId;
    private MultipartFile coverImage;
    private String fullDescription;

    @JsonIgnore
  private PublicationStatus publicationStatus=PublicationStatus.getById(publicationStatusId);


}
