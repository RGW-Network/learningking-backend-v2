package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanToCourseMapper;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link SubscriptionPlan}
 *
 * @author RayGdhrt
 *
 */
public interface SubscriptionPlanToCourseMapperService extends GenericService<SubscriptionPlanToCourseMapper> {

    public List<SubscriptionPlanToCourseMapper> getList(SubscriptionPlan plan);

    public List<SubscriptionPlanToCourseMapper> getList(Course course);

    public SubscriptionPlanToCourseMapper saveInstance(SubscriptionPlan plan, Course course) throws OperationFailedException, ValidationFailedException;
  public SubscriptionPlanToCourseMapper getRecord(SubscriptionPlan plan, Course course);

}
