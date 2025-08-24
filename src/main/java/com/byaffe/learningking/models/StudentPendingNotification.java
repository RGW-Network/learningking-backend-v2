package com.byaffe.learningking.models;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "pending_notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public class StudentPendingNotification extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    public StudentPendingNotification() {
    }

    public StudentPendingNotification(Student student, Notification notification) {
        this.student = student;
        this.notification = notification;
    }



    @Override
    public boolean equals(Object object) {
        return object instanceof StudentPendingNotification && (super.getId() != null) ? super.getId().equals(((StudentPendingNotification) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
