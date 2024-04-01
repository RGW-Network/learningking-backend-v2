package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
public class CourseTopicResponseDTO {
    private Long id;
    private String title;
    private String description;
    private int position = 1;
    private PublicationStatus publicationStatus;

    @JsonIgnore
    private CourseLesson courseLesson;


    private String publicationStatusName =publicationStatus!=null?publicationStatus.getDisplayName():null;
    private Integer publicationStatusId=publicationStatus!=null?publicationStatus.getId() :null;;


}
