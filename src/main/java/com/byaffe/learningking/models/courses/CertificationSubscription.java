package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.ReadStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "certification_subscriptions")
public class CertificationSubscription extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Column(name = "completed_courses")
    private int completedCourses = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_completed", length = 50)
    private Date dateCompleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", length = 20)
    private ReadStatus readStatus = ReadStatus.Inprogress;


    @Override
    public boolean equals(Object object) {
        return object instanceof CertificationSubscription && (super.getId() != null)
                ? super.getId().equals(((CertificationSubscription) object).getId())
                : (object == this);
    }


    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }


}
