package com.byaffe.learningking.dtos.courses;


import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.constants.RecordStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CourseCategoryRequestDTO {
    private Long id;
    private String name;
    private String colorCode;
    private String description;
    private String imageUrl;
    private CourseAcademyType academy;
    private CategoryType type;
    private RecordStatus recordStatus;
    private PublicationStatus publicationStatus;

    private MultipartFile image;
    private MultipartFile icon;
}
