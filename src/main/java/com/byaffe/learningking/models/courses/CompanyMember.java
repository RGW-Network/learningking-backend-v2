package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.shared.models.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "company_members")
public class CompanyMember extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String entryCode;
    private Member member;
    private Company company;
    private PublicationStatus publicationStatus=PublicationStatus.ACTIVE;

     @Column(name = "entry_code")
    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
    
    
  @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    @ManyToOne
     @JoinColumn(name = "member_id")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

     @OneToOne
     @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    
    @Override
    public boolean equals(Object object) {
        return object instanceof CompanyMember && (super.getId() != null) ? super.getId().equals(((CompanyMember) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
