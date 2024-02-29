package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.Payment;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanPayment;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.io.IOException;

/**
 * Responsible for CRUD operations on {@link Payment}
 *
 * @author Ray Gdhrt
 *
 */
public interface SubscriptionPlanPaymentService extends GenericService<SubscriptionPlanPayment> {

    

    SubscriptionPlanPayment createNewPaymentInstanceWithTransactionId(SubscriptionPlanPayment payment);
     SubscriptionPlanPayment initiatePayment(SubscriptionPlan subscriptionplan, Member member)throws IOException, OperationFailedException, ValidationFailedException ;
    
    /**
     *
     * @param transactionId
     * @return
     */
    SubscriptionPlanPayment getPaymentByTransactionId(String transactionId);
void updatePaymentStatus();
    /**
     *
     * @param transactionid
     * @param raveId
     * @return
     */
    SubscriptionPlanPayment updatePayment(String transactionid, String raveId)throws ValidationFailedException;
    
    /**
     * 
     */
     void checkAndUpdatePendingTransactions();

}
