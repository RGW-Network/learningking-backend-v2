package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanToCategoryMapper;
import com.byaffe.learningking.services.SubscriptionPlanToCategoryMapperService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscriptionPlanToCategoryMapperServiceImpl extends GenericServiceImpl<SubscriptionPlanToCategoryMapper> implements SubscriptionPlanToCategoryMapperService {
    
    @Override
    public boolean isDeletable(SubscriptionPlanToCategoryMapper entity) throws OperationFailedException {
        return true;
    }
    
    @Override
    public SubscriptionPlanToCategoryMapper saveInstance(SubscriptionPlanToCategoryMapper instance) throws ValidationFailedException, OperationFailedException {
        if (instance == null) {
            throw new ValidationFailedException("Null object");
        }
        
        if (instance.getCourseCategory() == null) {
            throw new ValidationFailedException("Invalid cost amount");
        }
        if (instance.getSubscriptionPlan() == null) {
            throw new ValidationFailedException("Missing Plan");
        }
        
        return super.save(instance);
        
    }
    
    @Override
    public SubscriptionPlanToCategoryMapper saveInstance(SubscriptionPlan plan, CourseCategory course) {
        SubscriptionPlanToCategoryMapper instance = new SubscriptionPlanToCategoryMapper();
        instance.setCourseCategory(course);
        instance.setSubscriptionPlan(plan);
        
        return super.save(instance);
        
    }
    
    @Override
    public List<SubscriptionPlanToCategoryMapper> getList(SubscriptionPlan plan) {
        return super.search(new Search()
                .addFilterEqual("subscriptionPlan", plan)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortDesc("dateCreated")
        );
    }
    
    @Override
    public List<SubscriptionPlanToCategoryMapper> getList(CourseCategory course) {
        return super.search(new Search()
                .addFilterEqual("courseCategory", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortDesc("dateCreated")
        );
    }
    
    public SubscriptionPlanToCategoryMapper getByName(String name) {
        return super.searchUnique(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("name", name)
                .setMaxResults(1)
        );
    }
    

    @Override
    public SubscriptionPlanToCategoryMapper getRecord(SubscriptionPlan plan, CourseCategory course) {
        return super.searchUnique(new Search()
                .addFilterEqual("subscriptionPlan", plan)
                .addFilterEqual("courseCategory", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortDesc("dateCreated")
                .setMaxResults(1)
        );
        
    }
    
}
