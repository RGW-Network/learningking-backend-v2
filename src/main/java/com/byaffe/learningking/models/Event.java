package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "lk_events")
public class Event extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id")
    private LookupValue category;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", nullable = true)
    private EventLocationType locationType = EventLocationType.PHYSICAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    private EventStatus status = EventStatus.UPCOMING;

    @Column(name="what_you_will_gain", columnDefinition = "TEXT")
    private String whatYouWillGain;

    @Column(name="start_time")
    private LocalTime startTime;
    @Column(name="end_time")
    private LocalTime endTime;

    @Column(name="start_date")
    private LocalDateTime startDate;
    @Column(name="end_date")
    private LocalDateTime endDate;

    @Column(name = "is_featured")
    private Boolean featured=false;
    @Column(name = "is_paid_for")
    private Boolean isPaidFor=false;

    @Column(name = "original_price")
    private Double originalPrice=0.0;
    @Column(name = "discounted_price")
    private Double discountedPrice=0.0;
    @Column(name = "maximum_attendees")
    private Long maximumAttendees;

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Event && (super.getId() != null) ? super.getId().equals(((Event) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
