package com.byaffe.learningking.models;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String profileImageUrl;
    private String location;
    private String bioInformation;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.Active;
    private String twitterHandle;
    private String facebookUsername;
    private String website;
    @ManyToOne
    @JoinColumn(name = "profession_id")
    private LookupValue profession;

    private String lastEmailVerificationCode;
    private String lastPhoneVerificationCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCodeSentAt;
    private String deviceId;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User userAccount;
    @ElementCollection
    @CollectionTable(name = "interest_names_mapping", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "list")
    private Set<String> interestNames;

    //Prospect fields
    @Column(name = "prospect_country_name")
    private String prospectCountryName;
    @Column(name = "prospect_first_name")
    private String prospectFirstName;
    @Column(name = "prospect_last_name")
    private String prospectLastName;
    @Column(name = "prospect_email")
    private String prospectLastEmail;
    @Column(name = "prospect_username")
    private String prospectUsername;
    @JsonIgnore
    @Column(name = "prospect_password")
    private String prospectPassword;

    @Transient
    public String getFullName() {
        return StringUtils.capitalize(this.prospectFirstName + " " + this.prospectLastName);
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
