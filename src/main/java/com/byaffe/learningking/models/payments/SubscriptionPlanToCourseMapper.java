package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.courses.Course;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.byaffe.learningking.shared.models.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "subscription_plans_to_course_mapping")
public class SubscriptionPlanToCourseMapper extends BaseEntity {

    private SubscriptionPlan subscriptionPlan;
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_plan_id")
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
 @Override
    public String toString() {
        return course==null?"": course.getTitle();
    }
    @Override
    public boolean equals(Object object) {
        return object instanceof SubscriptionPlanToCourseMapper && (super.getId() != null)
                ? super.getId().equals(((SubscriptionPlanToCourseMapper) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
