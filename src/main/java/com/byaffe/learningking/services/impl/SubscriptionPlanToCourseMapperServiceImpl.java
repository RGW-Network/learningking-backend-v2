package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanToCourseMapper;
import com.byaffe.learningking.services.SubscriptionPlanToCourseMapperService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscriptionPlanToCourseMapperServiceImpl extends GenericServiceImpl<SubscriptionPlanToCourseMapper> implements SubscriptionPlanToCourseMapperService {
    
    @Override
    public boolean isDeletable(SubscriptionPlanToCourseMapper entity) throws OperationFailedException {
        return true;
    }
    
    @Override
    public SubscriptionPlanToCourseMapper saveInstance(SubscriptionPlanToCourseMapper instance) throws ValidationFailedException, OperationFailedException {
        if (instance == null) {
            throw new ValidationFailedException("Null object");
        }
        
        if (instance.getCourse() == null) {
            throw new ValidationFailedException("Missing Course");
        }
        if (instance.getSubscriptionPlan() == null) {
            throw new ValidationFailedException("Missing Plan");
        }
        
        return super.save(instance);
        
    }
    
    @Override
    public SubscriptionPlanToCourseMapper saveInstance(SubscriptionPlan plan, Course course) throws ValidationFailedException, OperationFailedException {
        
        SubscriptionPlanToCourseMapper instance = new SubscriptionPlanToCourseMapper();
        instance.setCourse(course);
        instance.setSubscriptionPlan(plan);
        
        return saveInstance(instance);
        
    }
    
    @Override
    public List<SubscriptionPlanToCourseMapper> getList(SubscriptionPlan plan) {
        return super.search(new Search()
                .addFilterEqual("subscriptionPlan", plan)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortDesc("dateCreated")
        );
    }
    
    @Override
    public List<SubscriptionPlanToCourseMapper> getList(Course course) {
        return super.search(new Search()
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortDesc("dateCreated")
        );
    }
    
    public SubscriptionPlanToCourseMapper getByName(String name) {
        return super.searchUnique(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("name", name)
                .setMaxResults(1)
        );
    }
    

    
    @Override
    public SubscriptionPlanToCourseMapper getRecord(SubscriptionPlan plan, Course course) {
        return super.searchUnique(new Search()
                .addFilterEqual("subscriptionPlan", plan)
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortDesc("dateCreated")
                .setMaxResults(1)
        );
        
    }
    
}
