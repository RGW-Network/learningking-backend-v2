package com.byaffe.learningking.models.courses;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "company_courses")
public class CompanyCourse extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private int position;
    private Course course;
    private Company company;

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
     @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    
    @Override
    public boolean equals(Object object) {
        return object instanceof CompanyCourse && (super.getId() != null) ? super.getId().equals(((CompanyCourse) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
