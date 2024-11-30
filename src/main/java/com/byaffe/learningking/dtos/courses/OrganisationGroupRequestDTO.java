package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseOwnerShipType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrganisationGroupRequestDTO {
    private Long id;
    private String name;
    private String description;
    private Long organisationId;



}
