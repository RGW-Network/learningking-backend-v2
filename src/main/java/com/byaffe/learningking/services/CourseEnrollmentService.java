package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.models.payments.StudentSubscriptionPlan;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface CourseEnrollmentService extends GenericService<CourseEnrollment> {

    /**
     * 
     * @param member
     * @param serie
     * @return 
     */
    CourseEnrollment getSerieSubscription(Student member, Course serie);
    CourseEnrollment createSubscription(Student member, Course serie)throws ValidationFailedException;
     CourseEnrollment createSubscription(AggregatorTransaction coursePayment)throws ValidationFailedException;
     CourseEnrollment enrolForFreeCourse(Long studentId, Long courseId) throws ValidationFailedException;
    CourseEnrollment startCourse(Long studentId, Long courseId) throws ValidationFailedException;

    /**
     * 
     * @param member
     * @param courseTopic
     * @param
     * @return 
     * @throws ValidationFailedException
     */
    CourseEnrollment completeSubTopic(Student member, CourseLecture courseTopic)throws ValidationFailedException;

    /**
     * 
     * @param member
     * @return 
     */
 List<CourseEnrollment> getPlansForStudent(Student member);
 
   CourseEnrollment createSubscription(Course course, Student member) ;
   
  CourseEnrollment createActualSubscription(Course course, StudentSubscriptionPlan memberSubscriptionPlan) throws ValidationFailedException ;
    
}
