package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organisation_groups")
public class OrganisationStudentGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    @OneToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @OneToMany
    private List<OrganisationStudent> students= new ArrayList<>();

    @Override
    public boolean equals(Object object) {
        return object instanceof OrganisationStudentGroup && (super.getId() != null) ? super.getId().equals(((OrganisationStudentGroup) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
