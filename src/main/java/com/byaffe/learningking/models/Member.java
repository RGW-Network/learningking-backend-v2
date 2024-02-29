package com.byaffe.learningking.models;

import java.util.Date;
import java.util.Set;
import javax.persistence.CollectionTable;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.Region;
import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.User;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;

import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
public class Member extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String profileImageUrl;
    private String location;
    private String bioInformation;
    private AccountStatus accountStatus = AccountStatus.Active;
    private String twitterHandle;
    private String facebookUsername;
    private String website;
    private LookupValue profession;
      private Country country;
    private ProfessionValue professionValue;
    private String clearTextPassword;
    private String emailAddress;
    private String displayName;
    private String niceName;
    private String username;
    private String lastName;
    private String firstName;
    private Gender gender;
    private Region region;
    private String phoneNumber;
    private String paymentAttachmentUrl;
    private String lastEmailVerificationCode;
    
    private String lastPhoneVerificationCode;
     private Date lastCodeSentAt;
    private String deviceId;
    private User userAccount;

   private Set<String> interestNames;
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @ManyToOne
     @JoinColumn(name = "country_id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

     @OneToOne
     @JoinColumn(name = "user_id")
    public User getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }

    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "accountStatus", length = 20)
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_code_sent_at")
    public Date getLastCodeSentAt() {
        return lastCodeSentAt;
    }

    public void setLastCodeSentAt(Date lastCodeSentAt) {
        this.lastCodeSentAt = lastCodeSentAt;
    }

    @Column(name = "twitter_handle", length = 50)
    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    @Column(name = "facebook_username", length = 50)
    public String getFacebookUsername() {
        return facebookUsername;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }

    @Column(name = "website", length = 200)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Column(name = "bio", columnDefinition = "TEXT")
    public String getBioInformation() {
        return bioInformation;
    }

    public void setBioInformation(String bioInformation) {
        this.bioInformation = bioInformation;
    }

    public String composeFullName() {
        return StringUtils.capitalize(this.firstName + " " + this.lastName);
    }

    @ManyToOne
    @JoinColumn(name = "profession_id")
    public LookupValue getProfession() {
        return profession;
    }

    public void setProfession(LookupValue profession) {
        this.profession = profession;
    }

    @Column(name = "password")
    public String getClearTextPassword() {
        return clearTextPassword;
    }

    public void setClearTextPassword(String clearTextPassword) {
        this.clearTextPassword = clearTextPassword;
    }

    @Column(name = "email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gender")
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "last_email_verification_code")
    public String getLastEmailVerificationCode() {
        return lastEmailVerificationCode;
    }

    public void setLastEmailVerificationCode(String lastEmailVerificationCode) {
        this.lastEmailVerificationCode = lastEmailVerificationCode;
    }

    @Column(name = "last_phone_verification_code")
    public String getLastPhoneVerificationCode() {
        return lastPhoneVerificationCode;
    }

    public void setLastPhoneVerificationCode(String lastPhoneVerificationCode) {
        this.lastPhoneVerificationCode = lastPhoneVerificationCode;
    }

     @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

     @ElementCollection (fetch = FetchType.EAGER)
@CollectionTable(name="member_interests_names", joinColumns=@JoinColumn(name="member_id"))
@Column(name="interest_category_name")

    public Set<String> getInterestNames() {
        return interestNames;
    }

    public void setInterestNames(Set<String> interestNames) {
        this.interestNames = interestNames;
    }
    

    @Enumerated(EnumType.STRING)
    @Column(name = "profession_value")
    public ProfessionValue getProfessionValue() {
        return professionValue;
    }

    public void setProfessionValue(ProfessionValue professionValue) {
        this.professionValue = professionValue;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "regions")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Column(name = "payment_attachment_url")
    public String getPaymentAttachmentUrl() {
        return paymentAttachmentUrl;
    }

    public void setPaymentAttachmentUrl(String paymentAttachmentUrl) {
        this.paymentAttachmentUrl = paymentAttachmentUrl;
    }

    @Column(name = "display_name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Column(name = "nice_name")
    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   

    @Override
    public boolean equals(Object object) {
        return object instanceof Member && (super.getId() != null) ? super.getId().equals(((Member) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
