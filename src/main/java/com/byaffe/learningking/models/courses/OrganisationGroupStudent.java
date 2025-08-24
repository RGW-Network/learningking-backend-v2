package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

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

    @Transient
    public String getStudentName(){
        return organisationStudent.getStudent().getFullName();
    }

    @Transient
    public String getStudentEmail(){
        return organisationStudent.getStudent().getEmailAddress();
    }
    @Transient
    public String getStudentSerialNumber(){
        return organisationStudent.getStudent().getSerialNumber();
    }

    @Transient
    public String getStudentImageUrl(){
        return organisationStudent.getStudent().getProfileImageUrl();
    }

    @Transient
    public String getGroupName(){
        return organisationGroup.getName();
    }


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
