package com.byaffe.learningking.models.courses;


import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.smartcardio.ATR;

@Data
@Entity
@Table(name = "organisations")
public class Organisation extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String website;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "area_of_business_id")
    private LookupValue areaOfBusiness;
    private String emailAddress;
    private String coverImageUrl;
    private String logoImageUrl;
    private PublicationStatus publicationStatus = PublicationStatus.ACTIVE;

    @Transient
    public Long getAreaOfBusinessId() {
        return areaOfBusiness!=null?areaOfBusiness.getId():null;
    }

    @Transient
    public String getCountryName() {

        return country!=null? country.getName():null;
    }
    @Transient
    public Long getCountryId() {
        return country!=null?country.getId():null;
    }

    @Transient
    public String getAreaOfBusinessName() {

        return areaOfBusiness!=null? areaOfBusiness.getValue():null;
    }

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
