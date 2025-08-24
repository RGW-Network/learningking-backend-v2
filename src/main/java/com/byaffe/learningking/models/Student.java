package com.byaffe.learningking.models;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(name = "first_name", length = 50)
    private String firstName;
    @Column(name = "last_name", length = 50)
    private String lastName;
    @Column(name = "username", length = 50)
    private String username;
    @Column(name = "email_address", length = 50)
    private String emailAddress;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    @JsonIncludeProperties({"name","id"})
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.PendingActivation;
    private String coverImageUrl;
    private String profileImageUrl;
    private String location;
    private String bioInformation;
    private String twitterHandle;
    private String facebookUsername;
    private String instagramHandle;
    private String website;

    @JsonIncludeProperties({"value","id"})
    @ManyToOne
    @JoinColumn(name = "profession_id")
    private LookupValue profession;
    private String passKey;
    private String lastEmailVerificationCode;
    private String lastPhoneVerificationCode;
    private LocalDateTime lastCodeSentAt;
    private String deviceId;

    @JsonIncludeProperties({"username","id"})
    @OneToOne
    @JoinColumn(name = "user_id")
    private User userAccount;


    @ElementCollection
    @CollectionTable(name = "student_interest_names_mapping", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "list")
    private Set<String> interestNames;


    @Transient
    public String getFullName() {
        if(this.userAccount!=null) {
            return StringUtils.capitalize(this.userAccount.getFirstName() + " " + this.userAccount.getLastName());
        }
            return StringUtils.capitalize(this.firstName + " " + this.lastName);


    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Student && (super.getId() != null) ? super.getId().equals(((Student) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
