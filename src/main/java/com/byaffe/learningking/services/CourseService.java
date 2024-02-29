package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Communication;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Communication}
 *
 * @author RayGdhrt
 *
 */
public interface CourseService  extends GenericService<Course> {

    

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
     CourseSubTopic getFirstSubTopic(Course course) throws ValidationFailedException;
     
     /**
      * 
      * @param currentSubTopic
      * @return 
      */
     float getProgress(CourseSubTopic currentSubTopic);
    

}
