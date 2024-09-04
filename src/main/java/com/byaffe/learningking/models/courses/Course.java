package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(length = 1000)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String coverImageUrl;
    private String welcomeVideoUrl;
    private Boolean offersCertificate = false;

    @Column(name = "comma_separated_tags")
    private String commaSeparatedTags;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "what_you_will_learn", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "outcomes")
    private List<String> whatYouWillLearn;//new
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    @Column(name = "certificate_template", columnDefinition = "BIGTEXT")
    private String certificateTemplate;
    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus = PublicationStatus.ACTIVE;
    private LocalDate discountStartDate;//new
    private LocalDate discountEndDate;//new
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CourseCategory category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private CourseInstructor instructor;
    @Enumerated(EnumType.STRING)
    private CourseOwnerShipType ownershipType = CourseOwnerShipType.OPEN;
    @ManyToOne(optional = true)
    @JoinColumn(name = "owning_company")
    private Organisation organisation;
    @Enumerated(EnumType.STRING)
    private CourseAcademyType academy;
    private boolean isFeatured;
    private boolean isPaid;
    private Float price;
    private Float discountedPrice;
    private Float cost;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "course_testimonials", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "testimonial_id"))
    private Set<Testimonial> testimonials;

    public long getDaysToEndOfDiscount(){
        if(this.discountEndDate!=null){
            return ChronoUnit.DAYS.between(this.discountEndDate,LocalDate.now());
        }

        return 0;
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

    @Override
    public boolean equals(Object object) {
        return object instanceof Course && (super.getId() != null) ? super.getId().equals(((Course) object).getId())
                : (object == this);
    }
    public List<String> getTags() {
        if (StringUtils.isNotEmpty(this.commaSeparatedTags)) {
            return Arrays.asList(this.commaSeparatedTags.split(","));
        } else {
            return new ArrayList<>(); // Return an empty list if the string is empty or null
        }
    }
    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
