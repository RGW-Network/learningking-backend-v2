package com.byaffe.learningking.models.courses;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "organisation_group_courses")
public class OrganisationGroupCourse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToOne
    @JoinColumn(name = "organisation_group_id")
    private OrganisationGroup organisationGroup;


    
    @Override
    public boolean equals(Object object) {
        return object instanceof OrganisationGroupCourse && (super.getId() != null) ? super.getId().equals(((OrganisationGroupCourse) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
