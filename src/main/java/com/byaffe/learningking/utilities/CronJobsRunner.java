package com.byaffe.learningking.utilities;
import com.byaffe.learningking.services.PaymentService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @author User
 */
@Component
@Transactional
public class CronJobsRunner {

   
    @Scheduled(fixedDelay = 5000)//Every 5 seconds
    public void updateTransactionStatusTask() {
        System.out.println("Cron job to update course transaction statuses at "+new Date());
        ApplicationContextProvider.getBean(PaymentService.class).updatePaymentStatus();
    }
     @Scheduled(fixedDelay = 5000)//Every 5 seconds
    public void updateSubTransactionStatusTask() {
        System.out.println("Cron job to update subscription transaction statuses at "+new Date());
        ApplicationContextProvider.getBean(SubscriptionPlanPaymentService.class).updatePaymentStatus();
    }

    @Scheduled(fixedDelay = 5000)
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Fixed delay task ----------------------> " + System.currentTimeMillis() / 1000);
    }

}
