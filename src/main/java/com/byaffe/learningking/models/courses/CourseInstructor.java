package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;

import javax.persistence.*;

/**
 *
 * @author RayGdhrt
 */
@Entity
@Table(name = "instructors")
public class CourseInstructor extends BaseEntity {

    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private Country country;
    private String imageUrl;
    private Gender gender;
    private int numberOfContributions;

    @Column(name = "full_name", length = 50)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "email_addres", length = 50)
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = "phone_number", length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @ManyToOne
    @JoinColumn(name = "country_id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Column(name = "image_url", length = 500)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "number_of_contributions", length = 20)
    public int getNumberOfContributions() {
        return numberOfContributions;
    }

    public void setNumberOfContributions(int numberOfContributions) {
        this.numberOfContributions = numberOfContributions;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
     @Override
    public boolean equals(Object object) {
        return object instanceof CourseInstructor && (super.getId() != null) ? super.getId().equals(((CourseInstructor) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return  fullName + " (" + emailAddress + ")";
    }
    
    

}
