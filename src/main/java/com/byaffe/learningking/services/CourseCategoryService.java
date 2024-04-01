package com.byaffe.learningking.services;

import com.byaffe.learningking.controllers.dtos.CourseCategoryRequestDTO;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseCategory;

;

/**
 * Responsible for CRUD operations on {@link CourseCategory}
 *
 * @author RayGdhrt
 *
 */
public interface CourseCategoryService  extends GenericService<CourseCategory> {

  
    public CourseCategory getCategoryByAcademy(CourseAcademyType courseType) ;
    public CourseCategory saveInstance(CourseCategoryRequestDTO dto) ;

    public CourseCategory getCategoryByNameAndAcademy(String name, CourseAcademyType courseType) ;
}
