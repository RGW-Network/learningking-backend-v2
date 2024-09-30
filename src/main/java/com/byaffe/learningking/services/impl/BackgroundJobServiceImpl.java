package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.services.BackgroundJobService;
import com.byaffe.learningking.services.PaymentService;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackgroundJobServiceImpl implements BackgroundJobService {

    @Autowired
    private JobScheduler jobScheduler;

  @Autowired
  private PaymentService paymentService;

    public void registerBgJobs(){

        jobScheduler.scheduleRecurrently(Cron.minutely(),()-> paymentService.updatePaymentStatus());

    }



}