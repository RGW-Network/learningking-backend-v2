package com.byaffe.learningking.services;

import java.time.LocalDate;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.Payment;
import com.byaffe.learningking.models.Subscription;

/**
 * Responsible for CRUD operations on {@link Payment}
 *
 * @author Ray Gdhrt
 *
 */
public interface SubscriptionService extends GenericService<Subscription> {

    /**
     * 
     * @param payment
     * @return 
     */
    public Subscription createNewSubscription(Payment payment);
    
    
     /**
     * 
     * @param student
     * @return 
     */
    public Subscription getActiveSubscription(Student student);
    
    /**
     * 
     * @param student
     * @param startDate
     * @param attachment
     * @return 
     */
    public Subscription extendSubscription(Student student, LocalDate startDate, byte[] attachment) ;
   
    /**
     * 
     */
   void subscriptionObserver() ;

}
