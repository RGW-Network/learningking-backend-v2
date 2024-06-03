package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course_categories")
public class CourseCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "icon_url")
    private String iconUrl;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "academy")
    private CourseAcademyType academy;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private CategoryType type;

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
        return  name + "("+ academy +")" ;
    }
}
