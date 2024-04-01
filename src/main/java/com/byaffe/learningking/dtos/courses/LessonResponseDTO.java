package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class LessonResponseDTO {
    private Long id;
    private String title;
    private String description;
    private int position = 1;
    private boolean isLast = false;
    private boolean isFirst = false;
    private String coverImageUrl;
    private String videoUrl;
    private String audioUrl;
    private String fullDescription;
    private Course course;
    private PublicationStatus publicationStatus ;


    private String publicationStatusName =publicationStatus!=null?publicationStatus.getDisplayName():null;
    private Integer publicationStatusId=publicationStatus!=null?publicationStatus.getId() :null;;


}
