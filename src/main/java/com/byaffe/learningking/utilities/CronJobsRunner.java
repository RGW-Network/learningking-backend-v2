package com.byaffe.learningking.utilities;

import java.util.Date;
import org.byaffe.systems.core.services.PaymentService;
import org.byaffe.systems.core.services.SubscriptionPlanPaymentService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
