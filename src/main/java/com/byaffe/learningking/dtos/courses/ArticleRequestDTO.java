package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.models.courses.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Data
public class ArticleRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    private MultipartFile coverImage;
    private String fullDescription;
    private CourseAcademyType academy;
    private Long areaOfBusinessId;
    private PublicationStatus publicationStatus;
    private ArticleType type;
    private Long categoryId;
    private boolean featured;
    private boolean paid;
    private float cost;


}
