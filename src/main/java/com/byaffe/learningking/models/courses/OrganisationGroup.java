package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "organisation_groups")
public class OrganisationGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    @OneToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;


    @Override
    public boolean equals(Object object) {
        return object instanceof OrganisationGroup && (super.getId() != null) ? super.getId().equals(((OrganisationGroup) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
