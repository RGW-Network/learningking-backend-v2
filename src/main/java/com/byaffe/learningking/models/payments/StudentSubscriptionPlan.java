/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.payments;

import java.util.Date;
import javax.persistence.*;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

/**
 *
 * @author Ray Gdhrt
 */
@Data
@Entity
@Table(name = "student_subscription_plans")
public class StudentSubscriptionPlan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activated_on", nullable = false)
    private Date activatedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "depleted_on")
    private Date depletedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_on")
    private Date expiredOn;

    @Column(name = "duration_in_months", nullable = false)
    private int durationInMonths = 1;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionPlanStatus status = SubscriptionPlanStatus.ACTIVE;

    @Override
    public boolean equals(Object object) {
        return object instanceof StudentSubscriptionPlan && (super.getId() != null)
                ? super.getId().equals(((StudentSubscriptionPlan) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
