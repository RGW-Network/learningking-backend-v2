package com.byaffe.learningking.models;

import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wish_lists")
public class WishList extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Transient
    public String getCourseName() {
        if (this.course == null) {
            return null;
        }
        return course.getTitle();
    }

    @Transient
    public Long getCourseId() {
        if (this.course == null) {
            return null;
        }
        return course.getId();
    }
    @Transient
    public String getStudentName() {
        if (this.student == null) {
            return null;
        }
        return student.getFullName();
    }

    @Transient
    public Long getStudentId() {
        if (this.student == null) {
            return null;
        }
        return student.getId();
    }


    @Override
    public boolean equals(Object object) {
        return object instanceof WishList && (super.getId() != null) ? super.getId().equals(((WishList) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
