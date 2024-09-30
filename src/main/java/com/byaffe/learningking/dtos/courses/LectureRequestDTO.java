package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.ContentType;
import com.byaffe.learningking.models.courses.CourseTopic;
import com.byaffe.learningking.models.courses.ExternalResource;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Set;

@Data
public class LectureRequestDTO {
    private Long id=0L;
    private String title;
    private String body;
    private int position = 1;
    private String coverImageUrl;
    private String videoUrl;
    private String audioUrl;
    private String fullDescription;
    private Boolean isPreview = false;
    private Set<ExternalResource> externalLinks;
    private Long courseTopicId;
    private ContentType contentType;
    private MultipartFile coverImage;
    private PublicationStatus publicationStatus;


}
