package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "organisation_group_students")
public class OrganisationGroupStudent extends BaseEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organisation_student_id")
    private OrganisationStudent organisationStudent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organisation_group_id")
    private OrganisationGroup organisationGroup;

    @Override
    public boolean equals(Object object) {
        return object instanceof OrganisationGroupStudent && (super.getId() != null) ? super.getId().equals(((OrganisationGroupStudent) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
