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

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SubscriptionPlanToCourseMapperServiceImpl extends GenericServiceImpl<SubscriptionPlanToCourseMapper> implements SubscriptionPlanToCourseMapperService {
    
    @Override
    public boolean isDeletable(SubscriptionPlanToCourseMapper entity) throws OperationFailedException {
        return true;
    }

    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }


    @Override
    public SubscriptionPlanToCourseMapper saveInstance(SubscriptionPlan plan, Course course) throws ValidationFailedException, OperationFailedException {
        
        SubscriptionPlanToCourseMapper instance = new SubscriptionPlanToCourseMapper();
        instance.setCourse(course);
        instance.setSubscriptionPlan(plan);
        
        return save(instance);
        
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
