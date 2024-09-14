package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubscription;
import lombok.Data;

@Data
public class EnrollCourseResponseDTO  {
private Course course;
private CourseSubscription subscription;
  
}
