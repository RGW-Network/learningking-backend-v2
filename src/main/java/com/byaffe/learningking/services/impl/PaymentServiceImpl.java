package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.constants.TransactionType;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.models.payments.PaymentPrefixes;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.flutterwave.FlutterReponse;
import com.byaffe.learningking.services.flutterwave.FlutterWaveService;
import com.byaffe.learningking.services.flutterwave.FlutterwaveTransactionStatus;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.google.gson.Gson;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
public class PaymentServiceImpl extends GenericServiceImpl<AggregatorTransaction> implements PaymentService {

    @Autowired
    CourseService courseService;

    @Autowired
    FlutterWaveService flutterWaveService;
    private float eventPricing;


    @Override
    public AggregatorTransaction saveInstance(AggregatorTransaction payment) throws ValidationFailedException, OperationFailedException {

        return super.save(payment);
    }

    @Override
    public AggregatorTransaction createNewPaymentInstanceWithTransactionId(AggregatorTransaction payment) {

        return super.save(payment);
    }

    @Override
    public AggregatorTransaction updatePayment(String transactionid, String raveId) throws ValidationFailedException {
        AggregatorTransaction AggregatorTransaction = getPaymentByTransactionId(transactionid);
        if (AggregatorTransaction == null) {
            throw new ValidationFailedException("AggregatorTransaction not found");
        }
        AggregatorTransaction.setExternalReference(raveId);
        return super.save(AggregatorTransaction);

    }

    @Override
    public void checkAndUpdatePendingTransactions() {
        updatePaymentStatus();

    }

    @Override
    public List<AggregatorTransaction> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        }
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public AggregatorTransaction getPaymentByTransactionId(String storagePayment_id) {
        return super.searchUniqueByPropertyEqual("serialNumber", storagePayment_id);
    }


    @Override
    public boolean isDeletable(AggregatorTransaction entity) throws OperationFailedException {
        return false;

    }

    @Override
    public AggregatorTransaction initiateCoursePayment(long courseId, long studentId) throws IOException, OperationFailedException, ValidationFailedException {
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(courseId);
        Student student = ApplicationContextProvider.getBean(StudentService.class).getInstanceByID(studentId);
        CourseEnrollment memberCourse = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(student, course);
        if (memberCourse != null) {
            throw new ValidationFailedException("You already purchased this course. Go to My-Courses to view your course.");
        }
        return initiatePayment(course.getDiscountedPrice() > 0 ? course.getDiscountedPrice() : course.getPrice(), student, course.getTitle(), course.id, TransactionType.COURSE_PAYMENT);

    }
    public AggregatorTransaction initiateSubscriptionPlanPayment(long subscriptionPlanId, long studentId) throws IOException, OperationFailedException, ValidationFailedException {
        SubscriptionPlan subscriptionPlan = ApplicationContextProvider.getBean(SubscriptionPlanService.class).getInstanceByID(subscriptionPlanId);
        Student student = ApplicationContextProvider.getBean(StudentService.class).getInstanceByID(studentId);
        return initiatePayment(subscriptionPlan.getCostPerYear() > 0 ? subscriptionPlan.getCostPerYear() : subscriptionPlan.getCostPerMonth(), student, subscriptionPlan.getName(), subscriptionPlan.id, TransactionType.SUBSCRIPTION_PAYMENT);

    }
    public AggregatorTransaction initiateEventPayment(long subscriptionPlanId, long studentId) throws IOException, OperationFailedException, ValidationFailedException {
        Event event = ApplicationContextProvider.getBean(EventService.class).getInstanceByID(subscriptionPlanId);
        Student student = ApplicationContextProvider.getBean(StudentService.class).getInstanceByID(studentId);
        return initiatePayment(event.getDiscountedPrice() > 0 ? event.getDiscountedPrice() : event.getOriginalPrice(), student, event.getTitle(), event.id, TransactionType.SUBSCRIPTION_PAYMENT);

    }
    // Common method to initiate payments
    private AggregatorTransaction initiatePayment(double amount, Student student, String description, long referenceRecordId, TransactionType transactionType) throws IOException, OperationFailedException {
        AggregatorTransaction newPayment = new AggregatorTransaction();
        newPayment.setStudent(student);
        newPayment.setType(transactionType);
        newPayment.setTimestamp(LocalDateTime.now());
        newPayment.setReferenceRecordId(referenceRecordId);
        newPayment.setDescription("Payment For " + description);
        newPayment.setAmountInitiated(amount);
        newPayment.setAmountChargedFromUser(amount);
newPayment=super.save(newPayment);
newPayment.generateInternalReference();
        // Make Flutterwave request
        FlutterReponse flutterReponse = flutterWaveService.initiateDeposit(newPayment);
        newPayment.setLastAggregatorResponse(new Gson().toJson(flutterReponse));

        if (StringUtils.isNotBlank(flutterReponse.data.link)) {
            newPayment.setStatus(TransactionStatus.PENDING);
            newPayment.setRedirectUrl(flutterReponse.data.link);
        } else {
            newPayment.setStatus(TransactionStatus.FAILED);
            newPayment.setRedirectUrl(flutterReponse.message);
        }

        // Save payment request
        return saveInstance(newPayment);
    }
    public void updatePaymentStatus() {
        Search paymentSearch = new Search();
        log.debug("Started Payment Update job at " + LocalDateTime.now());
        paymentSearch.addFilterIn("status", Arrays.asList(
                TransactionStatus.PENDING, TransactionStatus.PENDING_3DS_AUTHORISATION,
                TransactionStatus.PENDING_OTP_VALIDATION,
                TransactionStatus.PENDING_PIN_AUTHORISATION,
                TransactionStatus.PENDING_AVS_AUTHORISATION));

        List<AggregatorTransaction> fetchedBookPayments = super.search(paymentSearch);
        for (AggregatorTransaction payment : fetchedBookPayments) {
            try {
                FlutterReponse flutterResponse = flutterWaveService.checkPaymentStatus(payment.getSerialNumber());
                updateStatus(flutterResponse, payment);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }


    private AggregatorTransaction updateStatus(FlutterReponse flutterResponse, AggregatorTransaction payment) {
        payment.setLastAggregatorResponse(new Gson().toJson(flutterResponse));
        payment.setDateChanged(LocalDateTime.now());

        //update failed validation
        if (Objects.equals(flutterResponse.status, "RequestFailed")) {
            payment.setStatus(TransactionStatus.PENDING);
            if (payment.getDateCreated().plusMinutes(30).isBefore(LocalDateTime.now())) {
                payment.setStatus(TransactionStatus.INDETERMINATE);
            }
            if ((flutterResponse.message.contains("No transaction was found for this id") || flutterResponse.status.equalsIgnoreCase("error")) && payment.getDateCreated().plusMinutes(30).isBefore(LocalDateTime.now())) {
                payment.setStatus(TransactionStatus.FAILED);
            }
        } else if (flutterResponse.data != null) {
            if (FlutterwaveTransactionStatus.SUCCESSFULL.getStatusName().equals(flutterResponse.data.status)) {
                log.debug("----Setting success payments------");
                payment.setStatus(TransactionStatus.SUCCESSFUL);
            } else if (FlutterwaveTransactionStatus.FAILED.getStatusName().equals(flutterResponse.data.status) || (FlutterwaveTransactionStatus.ERROR.getStatusName().equals(flutterResponse.status) && "Transaction not found".equals(flutterResponse.message))) {
                log.debug("----Setting failed payments------");
                payment.setStatus(TransactionStatus.FAILED);
            } else if (FlutterwaveTransactionStatus.CANCELLED.getStatusName().equals(flutterResponse.data.status) || FlutterwaveTransactionStatus.ABORTED.getStatusName().equals(flutterResponse.data.status)) {
                log.debug("----Setting cancelled payments------");
                payment.setStatus(TransactionStatus.CANCELED);
            } else if (Objects.equals(flutterResponse.data.status, "pending")) {
                log.debug("----Setting pending payments------");
                payment.setStatus(TransactionStatus.PENDING);
            }
        }
        payment = super.save(payment);
        if (payment.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
            if (payment.getType().equals(TransactionType.COURSE_PAYMENT)) {
                ApplicationContextProvider.getBean(CourseEnrollmentService.class).createSubscription(payment);
            } else if (payment.getType().equals(TransactionType.SUBSCRIPTION_PAYMENT)) {
                ApplicationContextProvider.getBean(StudentSubscriptionPlanService.class).activate(payment);
            }

        }


        return payment;
    }
    public static Search composeSearchObject(String searchTerm) {
        com.googlecode.genericdao.search.Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("serialNumber","description"));

        return search;
    }
}
