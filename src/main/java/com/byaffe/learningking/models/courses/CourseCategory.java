package com.byaffe.learningking.models.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "course_categories")
public class CourseCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String colorCode;
    private String description;
    private String imageUrl;
    private CourseAcademyType academy;
    private CategoryType type;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "academy")
    public CourseAcademyType getAcademy() {
        return academy;
    }

    public void setAcademy(CourseAcademyType academy) {
        this.academy = academy;
    }

      @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

     @Column(name = "color_code")
    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    
    
    @Override
    public boolean equals(Object object) {
        return object instanceof CourseCategory && (super.getId() != null) ? super.getId().equals(((CourseCategory) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return  name + "("+ academy+")" ;
    }
    
    
    
    

}
