package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.CourseRequestDTO;
import com.byaffe.learningking.models.Communication;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Communication}
 *
 * @author RayGdhrt
 *
 */
public interface CourseService  extends GenericService<Course> {

     Course saveInstance(CourseRequestDTO plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     * @throws ValidationFailedException
     */
    Course activatePlan(Course plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     */
    Course deActivatePlan(Course plan);

    /**
     *
     * @param planTitle
     * @return
     */
    Course getPlanByTitle(String planTitle);
    
    /**
     * 
     * @param course
     * @return
     * @throws ValidationFailedException 
     */
     CourseLecture getFirstSubTopic(Course course) throws ValidationFailedException;
     
     /**
      * 
      * @param currentSubTopic
      * @return 
      */
     float getProgress(CourseLecture currentSubTopic);
    

}
