package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.payments.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class StudentSubscriptionPlanServiceImpl extends GenericServiceImpl<StudentSubscriptionPlan> implements StudentSubscriptionPlanService {
  @Autowired
  SubscriptionPlanToCourseMapperService subscriptionPlanToCourseMapperService;
  
   @Autowired
   SubscriptionPlanToCategoryMapperService subscriptionPlanToCategoryMapperService;
    
    @Autowired
    CourseEnrollmentService courseSubscriptionService;

    @Override
    public boolean isDeletable(StudentSubscriptionPlan entity) throws OperationFailedException {
        return true;
    }

    @Override
    public StudentSubscriptionPlan saveInstance(StudentSubscriptionPlan instance) throws ValidationFailedException, OperationFailedException {
        if (instance == null) {
            throw new ValidationFailedException("Null object");
        }

        if (instance.getStudent() == null) {
            throw new ValidationFailedException("Invalid member");
        }
        if (instance.getSubscriptionPlan() == null) {
            throw new ValidationFailedException("Missing subscription");
        }

        return super.save(instance);

    }

    @Override
    public StudentSubscriptionPlan activate(AggregatorTransaction planPayment) throws ValidationFailedException {
SubscriptionPlan subscriptionPlan= ApplicationContextProvider.getBean(SubscriptionPlanService.class).getInstanceByID(planPayment.getReferenceRecordId());
        StudentSubscriptionPlan memberSubscriptionPlan = new StudentSubscriptionPlan();
        memberSubscriptionPlan.setSubscriptionPlan(subscriptionPlan);
        memberSubscriptionPlan.setStudent(planPayment.getStudent());
        memberSubscriptionPlan.setActivatedOn(new Date());
        memberSubscriptionPlan.setDurationInMonths(subscriptionPlan.getDurationInMonths());
        memberSubscriptionPlan.setCost(planPayment.getAmountInitiated());
        memberSubscriptionPlan.setStatus(SubscriptionPlanStatus.ACTIVE);

        return super.save(memberSubscriptionPlan);
    }

    @Override
    public CourseEnrollment payBySubscription(Course course, StudentSubscriptionPlan planPayment) throws ValidationFailedException {
        SubscriptionPlan subscriptionPlan = planPayment.getSubscriptionPlan();
        int takenWealthyMindsCourses = courseSubscriptionService.countInstances(new Search().addFilterEqual("student",planPayment.getStudent()));
         int takenCorporateCourses =  courseSubscriptionService.countInstances(new Search().addFilterEqual("student",planPayment.getStudent()));
        if (takenCorporateCourses<subscriptionPlan.getMaximumNumberOfCorporateCourses()) {
            return courseSubscriptionService.createActualSubscription(course, planPayment);

        }

        if (takenWealthyMindsCourses<subscriptionPlan.getMaximumNumberOfWealthyMindsCourses()) {
            if (subscriptionPlanToCategoryMapperService.getRecord(subscriptionPlan, course.getCategory())!=null) {
                return courseSubscriptionService.createActualSubscription(course, planPayment);
            }
        }

        throw new ValidationFailedException("This course doesn't apply to this subscription");
    }

    @Override
    public StudentSubscriptionPlan expire(StudentSubscriptionPlan plan) {
        plan.setExpiredOn(new Date());
        plan.setStatus(SubscriptionPlanStatus.EXPIRED);
        return super.save(plan);
    }

    @Override
    public StudentSubscriptionPlan deplete(StudentSubscriptionPlan plan) {
        plan.setDepletedOn(new Date());
        plan.setStatus(SubscriptionPlanStatus.DEPLETED);
        return super.save(plan);
    }

    public SubscriptionPlan getByName(String name) {
        return super.searchUnique(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("name", name)
                .setMaxResults(1)
        );
    }



    @Override
    public StudentSubscriptionPlan getInstance(Student student, SubscriptionPlan plan) throws ValidationFailedException {
        return super.searchUnique(
                new Search().addFilterEqual("member", student)
                        .addFilterEqual("subscriptionPlan", plan)
                        .addFilterEqual("status", SubscriptionPlanStatus.ACTIVE)
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .setMaxResults(1)
        );

    }

}
