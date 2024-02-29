package com.byaffe.learningking.models.courses;


import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;

import javax.persistence.*;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String name;
    private String abountDetails;
    private String website;
    private Country country;
    
       private LookupValue areaOfBusiness;
      private String emailAddress;
    private String coverImageUrl;
    private PublicationStatus publicationStatus=PublicationStatus.INACTIVE;

     @Column(name = "cover_image_url")
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
    
    
@ManyToOne
    @JoinColumn(name = "country")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @ManyToOne
    @JoinColumn(name = "area_of_business")
    public LookupValue getAreaOfBusiness() {
        return areaOfBusiness;
    }

    public void setAreaOfBusiness(LookupValue areaOfBusiness) {
        this.areaOfBusiness = areaOfBusiness;
    }

     @Column(name = "website")
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
 @Column(name = "email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

 

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

   
    @Column(name = "about_details", nullable = false,columnDefinition = "TEXT")
    public String getAbountDetails() {
        return abountDetails;
    }

    public void setAbountDetails(String abountDetails) {
        this.abountDetails = abountDetails;
    }
    

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  
  

    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Company() {
        super();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Company && (super.getId() != null) ? super.getId().equals(((Company) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
