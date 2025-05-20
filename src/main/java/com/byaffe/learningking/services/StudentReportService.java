package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.CourseCategoryRequestDTO;
import com.byaffe.learningking.dtos.student.StudentDashboardDto;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.CategoryType;

;

/**
 * Responsible for CRUD operations on {@link Category}
 *
 * @author RayGdhrt
 *
 */
public interface StudentReportService  {

  
    public StudentDashboardDto getStudentDashboard( ) ;


}
