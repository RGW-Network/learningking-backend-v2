package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseSubscription;
import com.byaffe.learningking.models.payments.CoursePayment;
import com.byaffe.learningking.models.payments.PaymentPrefixes;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.flutterwave.FlutterReponse;
import com.byaffe.learningking.services.flutterwave.FlutterwaveClient;
import com.byaffe.learningking.services.flutterwave.FlutterwaveTransactionStatus;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.google.gson.Gson;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class PaymentServiceImpl extends GenericServiceImpl<CoursePayment> implements PaymentService {

    @Autowired
    CourseService courseService;
    private float eventPricing;


    @Override
    public CoursePayment saveInstance(CoursePayment payment) throws ValidationFailedException, OperationFailedException {
        if (Float.isNaN(payment.getAmount())) {
            throw new ValidationFailedException("CoursePayment missing amount");
        }

        if (payment.isNew()) {
            payment = super.save(payment);
            payment.setTransactionId(PaymentPrefixes.COURSE_PAYMENT_PREFIX + String.valueOf( payment.getId()).toUpperCase());
        }
        return super.save(payment);
    }

    @Override
    public CoursePayment createNewPaymentInstanceWithTransactionId(CoursePayment payment) {

        return super.save(payment);
    }

    @Override
    public CoursePayment updatePayment(String transactionid, String raveId) throws ValidationFailedException {
        CoursePayment coursePayment = getPaymentByTransactionId(transactionid);
        if (coursePayment == null) {
            throw new ValidationFailedException("CoursePayment not found");
        }
        coursePayment.setRaveId(raveId);
        return super.save(coursePayment);

    }

    @Override
    public void checkAndUpdatePendingTransactions() {
        updatePaymentStatus();

    }

    @Override
    public List<CoursePayment> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        }
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public CoursePayment getPaymentByTransactionId(String storagePayment_id) {
        return super.searchUniqueByPropertyEqual("transactionId", storagePayment_id);
    }

    @Override
    public void updatePaymentStatus() {

        Search paymentSearch = new Search();
        paymentSearch.addFilterEqual("status", TransactionStatus.LINK_GENERATED);
        List<CoursePayment> fetchedCoursePayments = super.search(paymentSearch);

        for (CoursePayment payment : fetchedCoursePayments) {
            try {
                FlutterReponse flutterReponse = new FlutterwaveClient().checkPaymentStatusByTransactionId(payment.getTransactionId());

                payment.setLastPgwResponse(new Gson().toJson(flutterReponse));
                payment.setDateChanged(LocalDateTime.now());
                if (flutterReponse.status.equalsIgnoreCase("error")) {


                    payment.setStatus(TransactionStatus.FAILED);
                    saveInstance(payment);
                } else if (FlutterwaveTransactionStatus.SUCCESSFULL.getStatusName().equals(flutterReponse.data.status)) {


                    try {
                        ApplicationContextProvider.getBean(CourseSubscriptionService.class).createSubscription(payment);
                        payment.setStatus(TransactionStatus.SUCESSFULL);
                        saveInstance(payment);
                        ApplicationContextProvider.getBean(NotificationService.class)
                                .sendNotificationsToMember(
                                        new NotificationBuilder()
                                                .setTitle("Course Payment")
                                                .setDescription("Your payment for " + payment.getCourse().getTitle() + " has been recieved")
                                                .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                                                .setImageUrl(payment.getCourse().getCoverImageUrl())
                                                .setDestinationInstanceId(String.valueOf(payment.getCourse().getId()))
                                                .build(), payment.getSubscriber(),true);

                    } catch (Exception ex) {
                        payment.setFailureReason(ex.getLocalizedMessage());
                        payment.setStatus(TransactionStatus.FAILED);
                        saveInstance(payment);
                        Logger.getLogger(PaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else if (FlutterwaveTransactionStatus.FAILED.getStatusName().equals(flutterReponse.data.status) || (FlutterwaveTransactionStatus.ERROR.getStatusName().equals(flutterReponse.status) && "Transaction not found".equals(flutterReponse.message))) {

                    payment.setStatus(TransactionStatus.FAILED);
                    saveInstance(payment);

                } else if (FlutterwaveTransactionStatus.CANCELLED.getStatusName().equals(flutterReponse.data.status) || FlutterwaveTransactionStatus.ABORTED.getStatusName().equals(flutterReponse.data.status)) {

                    payment.setStatus(TransactionStatus.CANCELED);
                    saveInstance(payment);

                } else if (Duration.between(payment.getDateCreated(),LocalDateTime.now()).toDays() > 48) {

                    payment.setStatus(TransactionStatus.INDETERMINATE);

                    saveInstance(payment);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean isDeletable(CoursePayment entity) throws OperationFailedException {
        return false;

    }

    @Override
    public CoursePayment initiatePayment(Course course, Member member) throws IOException, OperationFailedException, ValidationFailedException {

        if (course == null) {
            throw new ValidationFailedException("Course Not Found");
        }

        CourseSubscription memberCourse = ApplicationContextProvider.getBean(CourseSubscriptionService.class).getSerieSubscription(member, course);
        if (memberCourse != null) {
            throw new ValidationFailedException("You already purchased this course. Go to My-Courses to view your course.");
        }

        SystemSetting setting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
        if (setting == null || setting.getFlutterwaveEncryptionKey() == null || setting.getFlutterwavePublicKey() == null) {
            throw new ValidationFailedException("Rave settings not yet configured");

        }
        CoursePayment newPayment = new CoursePayment();
        newPayment.setSubscriber(member);
        newPayment.setCourse(course);

        //set currency and amounts
        newPayment.setCurrency(setting.getBaseCurrency());
        newPayment.setAmount(course.getCost());
        newPayment.setTitle("Payment For " + course.getTitle());

       
        //make flutterwave request
        FlutterReponse flutterReponse = new FlutterwaveClient().requestPaymentInitiation(newPayment, member);

        if (StringUtils.isNotBlank(flutterReponse.data.link)) {
            newPayment.setStatus(TransactionStatus.LINK_GENERATED);
            newPayment.setLastRavePaymentLink(flutterReponse.data.link);
        } else {
            newPayment.setStatus(TransactionStatus.LINK_GENERATION_FAILED);
            newPayment.setLastRavePaymentLink(flutterReponse.message);
        }
        
          //save payment request
        return saveInstance(newPayment);
    }

}
