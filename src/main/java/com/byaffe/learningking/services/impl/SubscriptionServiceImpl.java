package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.SubscriptionStatus;
import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.models.EmailTemplate;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.Payment;
import com.byaffe.learningking.models.Subscription;
import com.byaffe.learningking.services.EmailTemplateService;
import com.byaffe.learningking.services.StudentService;
import com.byaffe.learningking.services.SubscriptionService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.MailService;
import com.byaffe.learningking.utilities.AppUtils;
import com.googlecode.genericdao.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class SubscriptionServiceImpl extends GenericServiceImpl<Subscription> implements SubscriptionService {
    
    @Override
    public boolean isDeletable(Subscription entity) throws OperationFailedException {
        return false;
    }
    
    @Override
    public Subscription saveInstance(Subscription instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
        
    }
    
    @Override
    public void subscriptionObserver() {
        
        List<Subscription> endingSubscriptions = getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("status", SubscriptionStatus.ACTIVE)
                .addFilterLessOrEqual("endDate", new Date()), 0, 0);
        
        for (Subscription subscription : endingSubscriptions) {
            Student student = subscription.getStudent();
            student.setAccountStatus(AccountStatus.Active);
            
            try {
                ApplicationContextProvider.getBean(StudentService.class).saveInstance(student);
                subscription.setStatus(SubscriptionStatus.STOPPED);
                saveInstance(subscription);
                ApplicationContextProvider.getBean(MailService.class).sendEmail(subscription.getStudent().getProspectEmailAddress(), "AAPU Subscription expired", "Your annual subscription has expired. Please renew for continued access to the AAPU services. Thank you");
                       
            } catch (ValidationFailedException | OperationFailedException ex) {
                Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        List<Subscription> upcomingSubscriptions = getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("status", SubscriptionStatus.SCHEDULED)
                .addFilterLessOrEqual("startDate", new Date()), 0, 0);
        
        for (Subscription subscription : upcomingSubscriptions) {
            Student student = subscription.getStudent();
            student.setAccountStatus(AccountStatus.Active);
            
            try {
                ApplicationContextProvider.getBean(StudentService.class).saveInstance(student);
                subscription.setStatus(SubscriptionStatus.ACTIVE);
                saveInstance(subscription);
            } catch (ValidationFailedException | OperationFailedException ex) {
                Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    @Override
    public Subscription getActiveSubscription(Student student) {
        
        Search search = new Search()
                .addFilterEqual("member", student)
                .addFilterEqual("status", SubscriptionStatus.ACTIVE);
        return super.searchUnique(search);
        
    }
    
    @Override
    public Subscription createNewSubscription(Payment payment) {
        if (payment == null || payment.getStudent() == null) {
            return null;
        }
        Subscription subscription = new Subscription();
        Subscription existingActiveSubscription = getActiveSubscription(payment.getStudent());
        subscription.setStartDate(LocalDate.now());
        if (existingActiveSubscription != null) {
            subscription.setStartDate(existingActiveSubscription.getEndDate().plusDays( 1));
            
        }
        
        subscription.setStudent(payment.getStudent());
        subscription.setPayment(payment);
        
        subscription.setEndDate(subscription.getStartDate().plusDays( 365));
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        final Subscription savedSubscription = subscription = super.save(subscription);
        Student student = savedSubscription.getStudent();
        student.setAccountStatus(AccountStatus.Active);
        try {
            ApplicationContextProvider.getBean(StudentService.class).quickSave(student);
        } catch (ValidationFailedException ex) {
            Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    System.out.println("Sending email...");
                    EmailTemplate emailTemplate = ApplicationContextProvider.getBean(EmailTemplateService.class)
                            .getEmailTemplateByType(TemplateType.SUCCESS_PAYMENT);
                    
                    if (emailTemplate != null) {
                        String html = emailTemplate.getTemplate();
                        
                        html = html.replace("{fullName}", savedSubscription.getStudent().getFullName());
                        html = html.replace("{transactionID}", savedSubscription.getPayment().getTransactionId());
                        ApplicationContextProvider.getBean(MailService.class).sendEmail(savedSubscription.getStudent().getProspectEmailAddress(), "AAPU Subscription", html);
                    } else {
                        ApplicationContextProvider.getBean(MailService.class).sendEmail(savedSubscription.getStudent().getProspectEmailAddress(), "AAPU Subscription", "Your subscription has been recieved");
                        
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
        return subscription;
        
    }
    
    @Override
    public Subscription extendSubscription(Student student, LocalDate startDate, byte[] attachment) {
        if (student == null|| attachment==null) {
            return null;
        }
        Subscription subscription = new Subscription();
        
        subscription.setStartDate(startDate);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        
        //Find existing active subscription
        Subscription existingActiveSubscription = getActiveSubscription(student);
        if (existingActiveSubscription != null) {
            
            //Check if exixting subscription is not due
            if (Duration.between(LocalDate.now(), existingActiveSubscription.getEndDate()).toDays() > 0) {
                subscription.setStartDate(existingActiveSubscription.getEndDate().plusDays(365));
                subscription.setStatus(SubscriptionStatus.SCHEDULED);
            } 
            //Else
            else {
                
                existingActiveSubscription.setStatus(SubscriptionStatus.STOPPED);
                super.save(existingActiveSubscription);
            }
        }
        
        subscription.setStudent(student);
        subscription.setPayment(null);
        System.out.println("Starting first save...");
        subscription = super.save(subscription);
         System.out.println("Starting image upload...");
        
        subscription.setAttachmentUrl(new AppUtils().uploadCloudinaryImage(attachment, "aapu_subscriptions/" + subscription.getId()));
        
        subscription.setEndDate(subscription.getStartDate().plusDays( 365));
        
        final Subscription savedSubscription =  super.save(subscription);
        
        student.setAccountStatus(AccountStatus.Active);
        try {
            ApplicationContextProvider.getBean(StudentService.class).quickSave(student);
        } catch (ValidationFailedException ex) {
            Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         System.out.println("Saved member...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    
                    System.out.println("Sending email...");

                    ApplicationContextProvider.getBean(MailService.class).sendEmail(savedSubscription.getStudent().getProspectEmailAddress(), "AAPU Subscription", "Congratulations,<br>Your annual subscription has been successfully extended to " + savedSubscription.getEndDate());
                    
                } catch (Exception ex) {
                    Logger.getLogger(SubscriptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
        return subscription;
        
    }
    
}
