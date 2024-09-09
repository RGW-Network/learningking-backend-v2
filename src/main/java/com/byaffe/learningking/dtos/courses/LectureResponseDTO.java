package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.*;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
public class LectureResponseDTO {
    private Long id;
    private String title;
    private String body;
    private int position = 1;
    private boolean isLast = false;
    private boolean isFirst = false;
    private String coverImageUrl;
    private String videoUrl;
    private String audioUrl;
    private String fullDescription;
    private ContentType contentType;
    private PublicationStatus publicationStatus;
    private Set<ExternalResource> externalLinks;
    private CourseTopic courseTopic;
    private String publicationStatusName = publicationStatus != null ? publicationStatus.getDisplayName() : null;
    private Integer publicationStatusId = publicationStatus != null ? publicationStatus.getId() : null;
    ;


}
