package com.byaffe.learningking.models.courses;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "course_certifications")
public class CertificationCourse extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private int position;
    private Course course;
    private Certification certification;

     @Column(name = "position")
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @ManyToOne
     @JoinColumn(name = "course_id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

     @OneToOne
     @JoinColumn(name = "certification_id")
    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    
    @Override
    public boolean equals(Object object) {
        return object instanceof CertificationCourse && (super.getId() != null) ? super.getId().equals(((CertificationCourse) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
