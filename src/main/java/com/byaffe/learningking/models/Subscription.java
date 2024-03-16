/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models;

import com.byaffe.learningking.constants.SubscriptionStatus;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author RayGdhrt
 */
@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {

    private LocalDate startDate;
    private LocalDate endDate;
    private int duration = 365;
    private Payment payment;
    private Student student;
    private String attachmentUrl;
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Student getMember() {
        return student;
    }

    public void setMember(Student student) {
        this.student = student;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "start_date", nullable = true)
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @OneToOne(optional = true)
    @JoinColumn(name = "payment_id", nullable = true)
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "end_date", nullable = true)
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Column(name = "duration", nullable = true)
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = true)
    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

     @Column(name = "attachement_url", nullable = true)
    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
    
    

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Subscription && (super.getId() != null)
                ? super.getId().equals(((Subscription) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
