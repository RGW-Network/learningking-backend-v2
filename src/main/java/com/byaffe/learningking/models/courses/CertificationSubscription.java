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
import com.byaffe.learningking.shared.models.BaseEntity;

/**
 *
 * @author Ray Gdhrt
 */
@Entity
@Table(name="certification_subscriptions")
public class CertificationSubscription extends BaseEntity {
    private Student student;
    private Certification certification;
    private int completedCourses=0;
    private Date dateCompleted;
    private ReadStatus readStatus= ReadStatus.Inprogress;
    
    

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")  
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "certification_id")
    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    @Column(name = "completed_courses")
    public int getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(int completedCourses) {
        this.completedCourses = completedCourses;
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
        return object instanceof CertificationSubscription && (super.getId() != null)
                ? super.getId().equals(((CertificationSubscription) object).getId())
                : (object == this);
    }

    
    
    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
      
    
    
    
}
