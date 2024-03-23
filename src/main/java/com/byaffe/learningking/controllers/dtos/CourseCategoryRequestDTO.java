package com.byaffe.learningking.controllers.dtos;


import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CourseCategoryRequestDTO {
    private Long id;
    private String name;
    private String colorCode;
    private String description;
    private String imageUrl;
    private Integer academyId;
    private Integer typeId;

@JsonIgnore
    public CourseAcademyType getAcademy(){return  CourseAcademyType.getById(this.academyId);};

@JsonIgnore
    public CategoryType getType(){return CategoryType.getById(this.typeId);};

    
}
