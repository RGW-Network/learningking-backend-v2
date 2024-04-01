package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.CourseCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @ManyToOne(optional = true)
    @JoinColumn(name = "area_of_business_id")
    private LookupValue areaOfBusiness;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = true)
    private ArticleType type = ArticleType.WEALTHY_MINDS_MAGAZINE;

    @ManyToOne(optional = true)
    @JoinColumn(name = "course_category")
    private CourseCategory category;

    @Column(name = "is_featured")
    private boolean featured;

    @Column(name = "is_paid")
    private boolean paid;

    @Column(name = "cost")
    private float cost;

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Article && (super.getId() != null) ? super.getId().equals(((Article) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
