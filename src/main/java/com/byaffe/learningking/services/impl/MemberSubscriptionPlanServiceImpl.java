package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubscription;
import com.byaffe.learningking.models.payments.*;
import com.byaffe.learningking.services.CourseSubscriptionService;
import com.byaffe.learningking.services.MemberSubscriptionPlanService;
import com.byaffe.learningking.services.SubscriptionPlanToCategoryMapperService;
import com.byaffe.learningking.services.SubscriptionPlanToCourseMapperService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class MemberSubscriptionPlanServiceImpl extends GenericServiceImpl<MemberSubscriptionPlan> implements MemberSubscriptionPlanService {
  @Autowired
  SubscriptionPlanToCourseMapperService subscriptionPlanToCourseMapperService;
  
   @Autowired
   SubscriptionPlanToCategoryMapperService subscriptionPlanToCategoryMapperService;
    
    @Autowired
    CourseSubscriptionService courseSubscriptionService;

    @Override
    public boolean isDeletable(MemberSubscriptionPlan entity) throws OperationFailedException {
        return true;
    }

    @Override
    public MemberSubscriptionPlan saveInstance(MemberSubscriptionPlan instance) throws ValidationFailedException, OperationFailedException {
        if (instance == null) {
            throw new ValidationFailedException("Null object");
        }

        if (instance.getMember() == null) {
            throw new ValidationFailedException("Invalid member");
        }
        if (instance.getSubscriptionPlan() == null) {
            throw new ValidationFailedException("Missing subscription");
        }

        return super.save(instance);

    }

    @Override
    public MemberSubscriptionPlan activate(SubscriptionPlanPayment planPayment) throws ValidationFailedException {

        MemberSubscriptionPlan memberSubscriptionPlan = new MemberSubscriptionPlan();
        memberSubscriptionPlan.setSubscriptionPlan(planPayment.getSubscriptionPlan());
        memberSubscriptionPlan.setMember(planPayment.getSubscriber());
        memberSubscriptionPlan.setActivatedOn(new Date());
        memberSubscriptionPlan.setDurationInMonths(planPayment.getSubscriptionPlan().getDurationInMonths());
        memberSubscriptionPlan.setCost(planPayment.getAmount());
        memberSubscriptionPlan.setStatus(SubscriptionPlanStatus.ACTIVE);

        return super.save(memberSubscriptionPlan);
    }

    @Override
    public CourseSubscription payBySubscription(Course course, MemberSubscriptionPlan planPayment) throws ValidationFailedException {
        SubscriptionPlan subscriptionPlan = planPayment.getSubscriptionPlan();

        if (subscriptionPlan.getContentRestrictionType().equals(SubscriptionContentRestrictionType.OPEN_ANY_COURSE)) {
            return courseSubscriptionService.createActualSubscription(course, planPayment);

        }

        if (planPayment.getSubscriptionPlan().getContentRestrictionType().equals(SubscriptionContentRestrictionType.SELECTED_CATEGORY)) {

            if (subscriptionPlanToCategoryMapperService.getRecord(subscriptionPlan, course.getCategory())!=null) {
                return courseSubscriptionService.createActualSubscription(course, planPayment);

            }

        }

        if (planPayment.getSubscriptionPlan().getContentRestrictionType().equals(SubscriptionContentRestrictionType.SELECTED_COURSES)) {

            if (subscriptionPlanToCourseMapperService.getRecord(subscriptionPlan, course)!=null) {
                return courseSubscriptionService.createActualSubscription(course, planPayment);

            }

        }

        if (planPayment.getSubscriptionPlan().getContentRestrictionType().equals(SubscriptionContentRestrictionType.SELECTED_ACADEMY)) {
            if (subscriptionPlan.getAllowedAcademyType().equals(course.getCategory().getAcademy())) {
                return courseSubscriptionService.createActualSubscription(course, planPayment);

            }
        }

        throw new ValidationFailedException("This course doesnt apply to this subscription");
    }

    @Override
    public MemberSubscriptionPlan expire(MemberSubscriptionPlan plan) {
        plan.setExpiredOn(new Date());
        plan.setStatus(SubscriptionPlanStatus.EXPIRED);
        return super.save(plan);
    }

    @Override
    public MemberSubscriptionPlan deplete(MemberSubscriptionPlan plan) {
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
    public MemberSubscriptionPlan getInstance(Member member, SubscriptionPlan plan) throws ValidationFailedException {
        return super.searchUnique(
                new Search().addFilterEqual("member", member)
                        .addFilterEqual("subscriptionPlan", plan)
                        .addFilterEqual("status", SubscriptionPlanStatus.ACTIVE)
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .setMaxResults(1)
        );

    }

}
