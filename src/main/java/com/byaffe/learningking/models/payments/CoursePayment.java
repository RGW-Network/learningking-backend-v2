package com.byaffe.learningking.models.payments;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "course_payments")  
@Inheritance(strategy = InheritanceType.JOINED)
public class CoursePayment extends BasePayment {

    private static final long serialVersionUID = 1L;

    private Course course;

    private Student subscriber;

  
    /**
     * @return the course
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "course_id", nullable = true)
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @return the subscriber
     */
    @OneToOne
    @JoinColumn(name = "subscriber")
    public Student getSubscriber() {
        return subscriber;
    }

    /**
     * @param subscriber the subscriber to set
     */
    public void setSubscriber(Student subscriber) {
        this.subscriber = subscriber;
    }

   
 @Override
    public String attachTransactionId() {
        if ( StringUtils.isBlank(transactionId)) {
            this.transactionId = PaymentPrefixes.COURSE_PAYMENT_PREFIX + String.valueOf( id).toUpperCase();
        }
        return transactionId;
    }
}
