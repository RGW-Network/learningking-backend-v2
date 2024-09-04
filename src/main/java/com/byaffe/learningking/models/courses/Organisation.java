package com.byaffe.learningking.models.courses;


import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "organisations")
public class Organisation extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String website;
    private Country country;
    private LookupValue areaOfBusiness;
    private String emailAddress;
    private String coverImageUrl;
    private String logoImageUrl;
    private PublicationStatus publicationStatus=PublicationStatus.INACTIVE;


    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Organisation && (super.getId() != null) ? super.getId().equals(((Organisation) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
