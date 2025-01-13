package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "certificate_templates")
public class CertificateTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "title")
    private String title;
    @Column(name = "template", columnDefinition = "TEXT")
    private String template;

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CertificateTemplate && (super.getId() != null) ? super.getId().equals(((CertificateTemplate) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
