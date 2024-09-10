/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "subscription_plans_to_category_mapping")
public class SubscriptionPlanToCategoryMapper extends BaseEntity {

    private SubscriptionPlan subscriptionPlan;
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_plan_id")
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
   
    public Category getCourseCategory() {
        return category;
    }

    public void setCourseCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category ==null?"": category.getName();
    }

    
    
    @Override
    public boolean equals(Object object) {
        return object instanceof SubscriptionPlanToCategoryMapper && (super.getId() != null)
                ? super.getId().equals(((SubscriptionPlanToCategoryMapper) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
