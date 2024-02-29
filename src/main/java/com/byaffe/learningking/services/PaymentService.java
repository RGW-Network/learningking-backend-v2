package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.Payment;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.payments.CoursePayment;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.io.IOException;

/**
 * Responsible for CRUD operations on {@link Payment}
 *
 * @author Ray Gdhrt
 *
 */
public interface PaymentService extends GenericService<CoursePayment> {

    

    CoursePayment createNewPaymentInstanceWithTransactionId(CoursePayment payment);
     CoursePayment initiatePayment(Course course, Member member)throws IOException, OperationFailedException, ValidationFailedException;
    
    /**
     *
     * @param transactionId
     * @return
     */
    CoursePayment getPaymentByTransactionId(String transactionId);
void updatePaymentStatus();
    /**
     *
     * @param transactionid
     * @param raveId
     * @return
     */
    CoursePayment updatePayment(String transactionid, String raveId)throws ValidationFailedException;
    
    /**
     * 
     */
     void checkAndUpdatePendingTransactions();

}
