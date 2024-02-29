package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.courses.Certification;
import com.byaffe.learningking.models.courses.CertificationSubscription;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface CertificationSubscriptionService extends GenericService<CertificationSubscription> {

    /**
     *
     * @param member
     * @param certification
     * @return
     */
    CertificationSubscription getSubscription(Member member, Certification certification);
    
    /**
     * 
     * @param member
     * @param course
     * @throws ValidationFailedException 
     */
   public void completeCertificationCourse(Member member, Course course) throws ValidationFailedException;
  
    /**
     *
     * @param member
     * @param certification
     * @return
     * @throws ValidationFailedException
     */
    CertificationSubscription createSubscription(Member member, Certification certification) throws ValidationFailedException;

    /**
     *
     * @param member
     * @return
     */
    List<CertificationSubscription> getSubscriptions(Member member);

    /**
     *
     * @param certification
     * @param member
     * @return
     */
    CertificationSubscription createSubscription(Certification certification, Member member);

}
