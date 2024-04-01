package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.SubscriptionContentRestrictionType;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.services.SubscriptionPlanService;
import com.byaffe.learningking.services.SubscriptionPlanToCourseMapperService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubscriptionPlanServiceImpl extends GenericServiceImpl<SubscriptionPlan> implements SubscriptionPlanService {

    @Autowired
    SubscriptionPlanToCourseMapperService subscriptionPlanToCourseMapperService;
    
    @Override
    public boolean isDeletable(SubscriptionPlan entity) throws OperationFailedException {
        return true;
    }

    @Override
    public SubscriptionPlan saveInstance(SubscriptionPlan instance) throws ValidationFailedException, OperationFailedException {
        if (instance == null) {
            throw new ValidationFailedException("Null object");
        }
        if (StringUtils.isBlank(instance.getName())) {
            throw new ValidationFailedException("Missing Name");
        }
        if (instance.getCost() <= 0) {
            throw new ValidationFailedException("Invalid cost amount");
        }
        if (instance.getDurationInMonths() <= 0) {
            throw new ValidationFailedException("Invalid expiry duration");
        }
        if (instance.getContentRestrictionType() == null) {
            throw new ValidationFailedException("Missing restriction type");
        }
        if (instance.getContentRestrictionType().equals(SubscriptionContentRestrictionType.SELECTED_ACADEMY)
                && instance.getAllowedAcademyType() == null) {
            throw new ValidationFailedException("Missing Allowed Academy");
        }


        if (instance.getContentRestrictionType().equals(SubscriptionContentRestrictionType.OPEN_ANY_COURSE)
                && (instance.getMaximumNumberOfCourses() <= 0)) {
            throw new ValidationFailedException("Missing Max number of courses");
        }

        SubscriptionPlan existsWithName = getByName(instance.getName());
        if (existsWithName != null && !existsWithName.getId().equals(instance.getId())) {
            throw new ValidationFailedException("Plan with Similar name exists");

        }
        return super.save(instance);

    }

    @Override
    public SubscriptionPlan activate(SubscriptionPlan plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);
        return super.save(plan);
    }

    @Override
    public SubscriptionPlan deActivate(SubscriptionPlan plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    public SubscriptionPlan getByName(String name) {
        return super.searchUnique(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("name", name)
                .setMaxResults(1)
        );
    }


}
