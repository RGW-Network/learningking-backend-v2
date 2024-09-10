package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.Category;

import javax.persistence.*;

import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "main_quote")
    private String mainQuote;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "area_of_business_id")
    private LookupValue areaOfBusiness;
    @Transient
public String  getAreaOfBusinessName(){
    return areaOfBusiness!=null? areaOfBusiness.getValue():null;
}
@Transient
    public Long  getAreaOfBusinessId(){
        return areaOfBusiness!=null? areaOfBusiness.getId():null;
    }
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus = PublicationStatus.INACTIVE;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = true)
    private ArticleType type = ArticleType.WEALTHY_MINDS_MAGAZINE;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "course_category")
    private Category category;

    @Transient
    public String  getCategoryName(){

        return category!=null? category.getName():null;
    }
    @Transient
    public String  getAcademyTypeName(){

        return type!=null? type.getDisplayName():null;
    }
    @Transient
    public Long  getCategoryId(){
        return   category!=null? category.getId():null;
    }
    @Column(name = "is_featured")
    private boolean featured;
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
