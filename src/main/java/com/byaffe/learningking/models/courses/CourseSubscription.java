/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models.courses;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.ReadStatus;
import com.byaffe.learningking.models.payments.MemberSubscriptionPlan;
import com.byaffe.learningking.shared.models.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name="course_subscriptions")
public class CourseSubscription extends BaseEntity {
    private Student student;
    private Course course;
      private CourseTopic currentSubTopic;
       private MemberSubscriptionPlan memberSubscriptionPlan;
    private int currentTopic=1;
    private int currentLesson=1;
    private Date dateCompleted;
     private String lastErrorMessage;
    private ReadStatus readStatus= ReadStatus.Inprogress;
    
    

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")  
    public Student getMember() {
        return student;
    }

    public void setMember(Student student) {
        this.student = student;
    }

     @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_subscription_plan_id")  
    public MemberSubscriptionPlan getMemberSubscriptionPlan() {
        return memberSubscriptionPlan;
    }

    public void setMemberSubscriptionPlan(MemberSubscriptionPlan memberSubscriptionPlan) {
        this.memberSubscriptionPlan = memberSubscriptionPlan;
    }
    
    

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

      @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_sub_topic_id")
  public CourseTopic getCurrentSubTopic() {
        return currentSubTopic;
    }

    public void setCurrentSubTopic(CourseTopic currentSubTopic) {
        this.currentSubTopic = currentSubTopic;
    }

    
    @Column(name = "current_topic")
    public int getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(int currentTopic) {
        this.currentTopic = currentTopic;
    }

      @Column(name = "current_lesson")
    public int getCurrentLesson() {
        return currentLesson;
    }

    public void setCurrentLesson(int currentLesson) {
        this.currentLesson = currentLesson;
    }

     @Column(name = "last_error_message")
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }
    
    

    @Temporal(TemporalType.TIMESTAMP)
     @Column(name="date_completed",length = 50)
    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status",length = 20)
    public ReadStatus getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(ReadStatus readStatus) {
        this.readStatus = readStatus;
    }

  
    
    @Override
    public boolean equals(Object object) {
        return object instanceof CourseSubscription && (super.getId() != null)
                ? super.getId().equals(((CourseSubscription) object).getId())
                : (object == this);
    }

    
    
    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
      
    
    
    
}
