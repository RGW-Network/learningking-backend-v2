package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "BIGTEXT")
    private String description;
    private int numberOfTopics = 0;
    private String coverImageUrl;
    private String welcomeVideoUrl;
    @Column(name="what_you_will_learn", columnDefinition = "BIGTEXT")
    private String whatYouWillLearn;

    @ElementCollection (fetch = FetchType.EAGER)
    @CollectionTable(name="course_tags", joinColumns=@JoinColumn(name="course_id"))
    @Column(name="tags")
    private List<String> tags;
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    @Column(name="certificate_template", columnDefinition = "BIGTEXT")
    private String certificateTemplate;
    @Enumerated(EnumType.STRING)
    private PublicationStatus publicationStatus = PublicationStatus.ACTIVE;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CourseCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CourseInstructor instructor;
    @Enumerated(EnumType.STRING)
    private CourseOwnerShipType ownershipType = CourseOwnerShipType.OPEN;
    @ManyToOne(optional = true)
    @JoinColumn(name = "owning_company")
    private Company company;
    @Enumerated(EnumType.STRING)
    private CourseAcademyType academy;
    private boolean isFeatured;
    private boolean isPaid;
    private Float price;
    private Float discountedPrice;
    private Float cost;
    @Column(columnDefinition = "LONGTEXT")
    private String fullDescription;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "course_testimonials", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "tesstimonial_id"))
    private Set<Testimonial> testimonials;

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

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
