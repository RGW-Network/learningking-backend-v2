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

@Entity
@Table(name = "articles")
public class Article extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private String coverImageUrl;
    private CourseCategory category;
    private boolean isFeatured;
    private boolean isPaid;
    private float cost;
    private LookupValue areaOfBusiness;
    private ArticleType type= ArticleType.WEALTHY_MINDS_MAGAZINE;
    private PublicationStatus publicationStatus=PublicationStatus.INACTIVE;


     @Column(name = "cover_image_url")
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "area_of_business_id")
    public LookupValue getAreaOfBusiness() {
        return areaOfBusiness;
    }

    public void setAreaOfBusiness(LookupValue areaOfBusiness) {
        this.areaOfBusiness = areaOfBusiness;
    }

   
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

     @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = true)
    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "course_category")
    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

   
    @Column(name = "description", columnDefinition = "TEXT")
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

    public Article() {
        super();
    }
 
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
