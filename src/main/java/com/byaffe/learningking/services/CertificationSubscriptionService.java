package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Certification;
import com.byaffe.learningking.models.courses.CertificationSubscription;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface CertificationSubscriptionService extends GenericService<CertificationSubscription> {

    /**
     *
     * @param student
     * @param certification
     * @return
     */
    CertificationSubscription getSubscription(Student student, Certification certification);
    
    /**
     * 
     * @param student
     * @param course
     * @throws ValidationFailedException 
     */
   public void completeCertificationCourse(Student student, Course course) throws ValidationFailedException;
  
    /**
     *
     * @param student
     * @param certification
     * @return
     * @throws ValidationFailedException
     */
    CertificationSubscription createSubscription(Student student, Certification certification) throws ValidationFailedException;

    /**
     *
     * @param student
     * @return
     */
    List<CertificationSubscription> getSubscriptions(Student student);

    /**
     *
     * @param certification
     * @param student
     * @return
     */
    CertificationSubscription createSubscription(Certification certification, Student student);

}
