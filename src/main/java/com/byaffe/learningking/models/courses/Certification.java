package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "certifications")
public class Certification extends BaseEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String coverImageUrl;
    @Column(columnDefinition = "TEXT")
    private String welcomeRemarks;
    @Column(columnDefinition = "TEXT")
    private String certificateTemplate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private CourseAcademyType academy;
    private boolean isFeatured;
    private boolean isSpecial = false;
    private boolean isPaid;
    private float cost;
    @Column(columnDefinition = "TEXT")
    private String fullDescription;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "certification_testimonials", joinColumns = @JoinColumn(name = "certification_id"), inverseJoinColumns = @JoinColumn(name = "testimonial_id"))
    private Set<Testimonial> testimonials;

    @Override
    public boolean equals(Object object) {
        return object instanceof Certification && (super.getId() != null)
                ? super.getId().equals(((Certification) object).getId())
                : (object == this);
    }


    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }


}
