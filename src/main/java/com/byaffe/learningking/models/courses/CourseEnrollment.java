package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.ReadStatus;
import com.byaffe.learningking.models.payments.StudentSubscriptionPlan;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "course_enrolments")
public class CourseEnrollment extends BaseEntity {

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_plan_id")
    private StudentSubscriptionPlan studentSubscriptionPlan;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_lecture_id")
    private CourseLecture currentLecture;

    @Column(name = "date_completed")
    private LocalDateTime dateCompleted;

    @Column(name = "last_error_message")
    private String lastErrorMessage;

    @Column(name = "progress")
    private Double progress;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status")
    private ReadStatus readStatus = ReadStatus.NotStarted;
    @Transient
    public String  getCurrentLectureName(){

        return currentLecture!=null? currentLecture.getTitle():null;
    }
    @Transient
    public Long  getCurrentLectureId(){

        return currentLecture!=null? currentLecture.getId():null;
    }
    @Transient
    public Long  getCourseId(){

        return course!=null? course.getId():null;
    }

    @Transient
    public String  getCourseName(){

        return course!=null? course.getTitle():null;
    }
    @Transient
    public Long  getStudentId(){

        return course!=null? course.getId():null;
    }

    @Transient
    public String  getStudentName(){

        return student!=null? student.getFullName():null;
    }

    @Transient
    public Long  getCourseLessonId(){

        return currentLecture!=null? currentLecture.getId():null;
    }

    @Transient
    public String  getCurrentTopicName(){

        return currentLecture!=null? currentLecture.getCourseTopic().getTitle():null;
    }
    @Transient
    public Long  getCourseTopicId(){

        return currentLecture!=null? currentLecture.getCourseTopic().getId():null;
    }

    @Transient
    public String  getCurrentLessonName(){

        return currentLecture!=null? currentLecture.getCourseTopic().getCourseLessonName():null;
    }
    @Transient
    public Long  getCurrentLessonId(){

        return currentLecture!=null? currentLecture.getCourseTopic().getCourseLessonId():null;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CourseEnrollment && (super.getId() != null)
                ? super.getId().equals(((CourseEnrollment) object).getId())
                : (object == this);
    }


    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }


}
