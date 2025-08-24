package com.byaffe.learningking.models;

import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "event_attendances")
public class EventAttendance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "aggregator_transaction_id")
    private AggregatorTransaction aggregatorTransaction;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventAttendanceStatus status;



    @Transient
    public String getEventName() {
        if (this.event == null) {
            return null;
        }
        return event.getTitle();
    }

    @Transient
    public Long getEventId() {
        if (this.event == null) {
            return null;
        }
        return event.getId();
    }
    @Transient
    public String getStudentName() {
        if (this.student == null) {
            return null;
        }
        return student.getFullName();
    }

    @Transient
    public Long getStudentId() {
        if (this.student == null) {
            return null;
        }
        return student.getId();
    }




    @Override
    public boolean equals(Object object) {
        return object instanceof EventAttendance && (super.getId() != null) ? super.getId().equals(((EventAttendance) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
