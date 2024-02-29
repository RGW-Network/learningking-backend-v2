package com.byaffe.learningking.services;

import java.time.LocalDate;
import java.util.Date;
import com.byaffe.learningking.models.Member;
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
     * @param member
     * @return 
     */
    public Subscription getActiveSubscription(Member member);
    
    /**
     * 
     * @param member
     * @param startDate
     * @param attachment
     * @return 
     */
    public Subscription extendSubscription(Member member, LocalDate startDate, byte[] attachment) ;
   
    /**
     * 
     */
   void subscriptionObserver() ;

}
