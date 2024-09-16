package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.payments.StudentSubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanPayment;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link SubscriptionPlan}
 *
 * @author RayGdhrt
 *
 */


public interface StudentSubscriptionPlanService extends GenericService<StudentSubscriptionPlan> {

    public StudentSubscriptionPlan activate(SubscriptionPlanPayment  subscriptionPlanPayment) throws ValidationFailedException;
  public StudentSubscriptionPlan getInstance(Student student, SubscriptionPlan plan) throws ValidationFailedException;

    
     public StudentSubscriptionPlan expire(StudentSubscriptionPlan plan);
    
     public StudentSubscriptionPlan deplete(StudentSubscriptionPlan plan);
     
      public CourseEnrollment payBySubscription(Course course, StudentSubscriptionPlan planPayment) throws ValidationFailedException;
   
}
