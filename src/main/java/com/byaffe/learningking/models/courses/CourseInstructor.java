package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "instructors")
@Data
public class CourseInstructor extends BaseEntity {

    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name", length = 50)
    private String lastName;
    @Column(name = "username", length = 50)
    private String username;
    @Column(name = "email_address", length = 50)
    private String emailAddress;
    @Column(name = "designation")
    private String designation;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.PendingActivation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "image_url", length = 500)
    private String imageUrl;
    @Column(name = "biography", length = 2000)
    private String biography;
    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_value")
    private Gender gender;
    @Column(name = "number_of_contributions", length = 20)
    private int numberOfContributions;
    @Column(name = "last_verification", length = 20)
    private String lastVerificationCode;

    @JsonIncludeProperties({"id", "username", "emailAddress","roles"})
    @OneToOne
    @JoinColumn(name = "user_id")
    private User userAccount;

    @Transient
    public Set<Long> getUserAccountRoles() {
        Set<Long> ids = new HashSet<>();
        if (this.userAccount != null) {
            for (Role role : userAccount.getRoles()) {
                ids.add(role.getId());
            }
        }
        return ids;
    }

    @Transient
    public String getCountryName() {
        return this.country != null ? country.getName() : null;
    }

    @Transient
    public Long getCountryId() {
        return this.country != null ? country.getId() : null;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + emailAddress + ")";
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CourseInstructor && (super.getId() != null) ? super.getId().equals(((CourseInstructor) object).getId())
                : (object == this);
    }

    @Transient
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
