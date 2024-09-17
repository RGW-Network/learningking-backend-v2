package com.byaffe.learningking.services;

import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.io.IOException;

/**
 * Responsible for CRUD operations on {@link AggregatorTransaction}
 *
 * @author Ray Gdhrt
 *
 */
public interface PaymentService extends GenericService<AggregatorTransaction> {

    

    AggregatorTransaction createNewPaymentInstanceWithTransactionId(AggregatorTransaction payment);
     AggregatorTransaction initiateCoursePayment(long course, long student)throws IOException, OperationFailedException, ValidationFailedException;
    AggregatorTransaction initiateSubscriptionPlanPayment(long course, long student)throws IOException, OperationFailedException, ValidationFailedException;
    AggregatorTransaction initiateEventPayment(long event, long student)throws IOException, OperationFailedException, ValidationFailedException;

    /**
     *
     * @param transactionId
     * @return
     */
    AggregatorTransaction getPaymentByTransactionId(String transactionId);
void updatePaymentStatus();
    /**
     *
     * @param transactionid
     * @param raveId
     * @return
     */
    AggregatorTransaction updatePayment(String transactionid, String raveId)throws ValidationFailedException;
    
    /**
     * 
     */
     void checkAndUpdatePendingTransactions();

}
