package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.constants.PreferredModeOfDelivery;
import com.byaffe.learningking.models.SubmissionStatus;
import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

@Data
public class CustomCourseRequestDTO {
    private Long id;
    private String courseName;
    private Long targetTrainees;
    private Long courseCategoryId;//category
    private PreferredModeOfDelivery preferredModeOfDelivery;//enum
    private String courseDetails;
    private String expectedOutComes;
    //private SubmissionStatus status;

}
