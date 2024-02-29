package com.byaffe.learningking.models;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "member_pending_notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberPendingNotification extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Member member;

    private Notification notification;

    public MemberPendingNotification() {
    }

    public MemberPendingNotification(Member member, Notification notification) {
        this.member = member;
        this.notification = notification;
    }

    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @ManyToOne
    @JoinColumn(name = "notification_id")
    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof MemberPendingNotification && (super.getId() != null) ? super.getId().equals(((MemberPendingNotification) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
