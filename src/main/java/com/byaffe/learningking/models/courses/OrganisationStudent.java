package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "organisation_students")
public class OrganisationStudent extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @Override
    public boolean equals(Object object) {
        return object instanceof OrganisationStudent && (super.getId() != null) ? super.getId().equals(((OrganisationStudent) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
