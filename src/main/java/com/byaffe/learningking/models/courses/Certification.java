package com.byaffe.learningking.models.courses;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "certifications")
public class Certification extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private String coverImageUrl;
    private String welcomeRemarks;
    private String certificateTemplate;
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;
    private CourseAcademyType academy;
    private boolean isFeatured;
    private boolean isSpecial = false;
    private boolean isPaid;
    private float cost;
    private String fullDescription;
    private Set<Testimonial> testimonials;

    @Column(name = "full_description", columnDefinition = "TEXT")
    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    @Column(name = "is_special")
    public boolean isIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    @Column(name = "cover_image_url")
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

     @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "certification_testimonials", joinColumns = @JoinColumn(name = "certification_id"), inverseJoinColumns = @JoinColumn(name = "tesstimonial_id"))
  
    public Set<Testimonial> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(Set<Testimonial> testimonials) {
        this.testimonials = testimonials;
    }

    public void addTestimonial(Testimonial testimonial) {
        if (this.testimonials == null) {
            this.testimonials = new HashSet<>();
        }
        this.testimonials.add(testimonial);
    }

    public void removeTestimonial(Testimonial testimonial) {
        if (this.testimonials != null) {
            this.testimonials.remove(testimonial);
        }
    }

    @Column(name = "welcome_remarks", columnDefinition = "TEXT")
    public String getWelcomeRemarks() {
        return welcomeRemarks;
    }

    public void setWelcomeRemarks(String welcomeRemarks) {
        this.welcomeRemarks = welcomeRemarks;
    }

    @Column(name = "certificate_template", columnDefinition = "TEXT")
    public String getCertificateTemplate() {
        return certificateTemplate;
    }

    public void setCertificateTemplate(String certificateTemplate) {
        this.certificateTemplate = certificateTemplate;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "academy")
    public CourseAcademyType getAcademy() {
        return academy;
    }

    public void setAcademy(CourseAcademyType academy) {
        this.academy = academy;
    }

    @Column(name = "is_featured")
    public boolean isIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    @Column(name = "is_paid")
    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    @Column(name = "cost")
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Certification() {
        super();
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Certification && (super.getId() != null) ? super.getId().equals(((Certification) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
