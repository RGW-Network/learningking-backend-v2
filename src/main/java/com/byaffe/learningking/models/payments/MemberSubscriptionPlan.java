/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.payments;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.shared.models.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name = "member_subscription_plans")
public class MemberSubscriptionPlan extends BaseEntity {

    private Member member;
    private SubscriptionPlan subscriptionPlan;
    private Date activatedOn;
    private Date depletedOn;
    private Date expiredOn;
    private int durationInMonths = 1;
    private float cost;
    private SubscriptionPlanStatus status = SubscriptionPlanStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @ManyToOne
    @JoinColumn(name = "subscription_plan_id")
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activated_on")
    public Date getActivatedOn() {
        return activatedOn;
    }

    public void setActivatedOn(Date activatedOn) {
        this.activatedOn = activatedOn;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "depleted_on")
    public Date getDepletedOn() {
        return depletedOn;
    }

    public void setDepletedOn(Date depletedOn) {
        this.depletedOn = depletedOn;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_on")
    public Date getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(Date expiredOn) {
        this.expiredOn = expiredOn;
    }

    @Column(name = "duration_in_months")
    public int getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    @Column(name = "cost")
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public SubscriptionPlanStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionPlanStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof MemberSubscriptionPlan && (super.getId() != null)
                ? super.getId().equals(((MemberSubscriptionPlan) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
