package com.byaffe.learningking.services;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.courses.CourseRating;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.util.List;

;

/**
 * Responsible for CRUD operations on {@link CourseCategory}
 *
 * @author RayGdhrt
 *
 */
public interface CourseRatingService  extends GenericService<CourseRating> {

    
    public CourseRating activate(CourseRating plan) throws ValidationFailedException;

    public CourseRating deActivate(CourseRating plan);
    
     public List<CourseRating> getCourseRatings(Course course);
     
      public double getTotalCourseRatings(Course course);
      public int getRatingsCount(Course course);
   

}
