package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanToCategoryMapper;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link SubscriptionPlan}
 *
 * @author RayGdhrt
 *
 */
public interface SubscriptionPlanToCategoryMapperService extends GenericService<SubscriptionPlanToCategoryMapper> {

    public List<SubscriptionPlanToCategoryMapper> getList(SubscriptionPlan plan);

    public List<SubscriptionPlanToCategoryMapper> getList(CourseCategory course);

    public SubscriptionPlanToCategoryMapper saveInstance(SubscriptionPlan plan, CourseCategory course) throws ValidationFailedException;
  public SubscriptionPlanToCategoryMapper getRecord(SubscriptionPlan plan, CourseCategory course);

}
