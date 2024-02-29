package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.models.courses.CourseSubscription;
import com.byaffe.learningking.models.courses.CourseTopic;
import com.byaffe.learningking.models.payments.CoursePayment;
import com.byaffe.learningking.models.payments.MemberSubscriptionPlan;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface CourseSubscriptionService extends GenericService<CourseSubscription> {

    /**
     * 
     * @param member
     * @param serie
     * @return 
     */
    CourseSubscription getSerieSubscription(Member member, Course serie);
    CourseSubscription createSubscription(Member member, Course serie)throws ValidationFailedException;
     CourseSubscription createSubscription(CoursePayment coursePayment)throws ValidationFailedException;
    
      /**
     * 
     * @param member
     * @param courseTopic
     * @param
     * @return 
     * @throws ValidationFailedException
     */
    CourseSubscription completeSubTopic(Member member,CourseSubTopic courseTopic)throws ValidationFailedException;

    /**
     * 
     * @param member
     * @return 
     */
 List<CourseSubscription> getPlansForMember(Member member);
 
   CourseSubscription createSubscription(Course course, Member member) ;
   
  CourseSubscription createActualSubscription(Course course, MemberSubscriptionPlan memberSubscriptionPlan) throws ValidationFailedException ;
    
}
