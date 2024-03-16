package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubscription;
import com.byaffe.learningking.models.payments.MemberSubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanPayment;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link SubscriptionPlan}
 *
 * @author RayGdhrt
 *
 */


public interface MemberSubscriptionPlanService extends GenericService<MemberSubscriptionPlan> {

    public MemberSubscriptionPlan activate(SubscriptionPlanPayment  subscriptionPlanPayment) throws ValidationFailedException;
  public MemberSubscriptionPlan getInstance(Student student, SubscriptionPlan plan) throws ValidationFailedException;

    
     public MemberSubscriptionPlan expire(MemberSubscriptionPlan plan);
    
     public MemberSubscriptionPlan deplete(MemberSubscriptionPlan plan);
     
      public CourseSubscription payBySubscription(Course course, MemberSubscriptionPlan planPayment) throws ValidationFailedException;
   
}
