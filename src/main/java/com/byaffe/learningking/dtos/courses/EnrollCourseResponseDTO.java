package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import lombok.Data;

@Data
public class EnrollCourseResponseDTO  {
private Course course;
private CourseEnrollment subscription;
  
}
