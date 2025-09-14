package com.byaffe.learningking.utilities;
import com.byaffe.learningking.services.PaymentService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @author User
 */
@Component
public class CronJobsRunner {
@Autowired
PaymentService paymentService;
   
    @Scheduled(fixedDelay = 5000)//Every 5 seconds
    public void updateTransactionStatusTask() {
        paymentService.updatePaymentStatus();
    }



}
