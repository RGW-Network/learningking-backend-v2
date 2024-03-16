package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.models.courses.CourseSubscription;
import com.byaffe.learningking.models.courses.CourseTopic;
import com.byaffe.learningking.models.payments.CoursePayment;
import com.byaffe.learningking.models.payments.StudentSubscriptionPlan;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface CourseSubscriptionService extends GenericService<CourseSubscription> {

    /**
     * 
     * @param member
     * @param serie
     * @return 
     */
    CourseSubscription getSerieSubscription(Student member, Course serie);
    CourseSubscription createSubscription(Student member, Course serie)throws ValidationFailedException;
     CourseSubscription createSubscription(CoursePayment coursePayment)throws ValidationFailedException;
    
      /**
     * 
     * @param member
     * @param courseTopic
     * @param
     * @return 
     * @throws ValidationFailedException
     */
    CourseSubscription completeSubTopic(Student member,CourseSubTopic courseTopic)throws ValidationFailedException;

    /**
     * 
     * @param member
     * @return 
     */
 List<CourseSubscription> getPlansForStudent(Student member);
 
   CourseSubscription createSubscription(Course course, Student member) ;
   
  CourseSubscription createActualSubscription(Course course, StudentSubscriptionPlan memberSubscriptionPlan) throws ValidationFailedException ;
    
}
