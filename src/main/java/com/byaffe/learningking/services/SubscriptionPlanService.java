package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.SubscriptionPlanRequestDTO;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link SubscriptionPlan}
 *
 * @author RayGdhrt
 *
 */


public interface SubscriptionPlanService extends GenericService<SubscriptionPlan> {

    public SubscriptionPlan activate(SubscriptionPlan plan) throws ValidationFailedException;
    public SubscriptionPlan saveInstance(SubscriptionPlanRequestDTO plan) throws ValidationFailedException;



    public SubscriptionPlan deActivate(SubscriptionPlan plan);
}
