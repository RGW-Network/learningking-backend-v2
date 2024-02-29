package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.models.payments.MemberSubscriptionPlan;
import com.byaffe.learningking.models.payments.PaymentPrefixes;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlanPayment;
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
public class SubscriptionPlanPaymentServiceImpl extends GenericServiceImpl<SubscriptionPlanPayment> implements SubscriptionPlanPaymentService {

    @Autowired
    SubscriptionPlanService subscriptionplanService;
    private float eventPricing;


    @Override
    public SubscriptionPlanPayment saveInstance(SubscriptionPlanPayment payment) throws ValidationFailedException, OperationFailedException {
        if (Float.isNaN(payment.getAmount())) {
            throw new ValidationFailedException("SubscriptionPlanPayment missing amount");
        }

        if (payment.isNew()) {
            payment = super.save(payment);
            payment.setTransactionId(PaymentPrefixes.SUBSCRIPTION_PAYMENT_PREFIX + String.valueOf( payment.getId()).toUpperCase());
        }
        return super.save(payment);
    }

    @Override
    public SubscriptionPlanPayment createNewPaymentInstanceWithTransactionId(SubscriptionPlanPayment payment) {

        return super.save(payment);
    }

    @Override
    public SubscriptionPlanPayment updatePayment(String transactionid, String raveId) throws ValidationFailedException {
        SubscriptionPlanPayment subscriptionplanPayment = getPaymentByTransactionId(transactionid);
        if (subscriptionplanPayment == null) {
            throw new ValidationFailedException("SubscriptionPlanPayment not found");
        }
        subscriptionplanPayment.setRaveId(raveId);
        return super.save(subscriptionplanPayment);

    }

    @Override
    public void checkAndUpdatePendingTransactions() {
        updatePaymentStatus();

    }

    @Override
    public List<SubscriptionPlanPayment> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        }
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public SubscriptionPlanPayment getPaymentByTransactionId(String storagePayment_id) {
        return super.searchUniqueByPropertyEqual("transactionId", storagePayment_id);
    }

    @Override
    public void updatePaymentStatus() {
        Search paymentSearch = new Search();
        paymentSearch.addFilterEqual("status", TransactionStatus.LINK_GENERATED);
        List<SubscriptionPlanPayment> fetchedSubscriptionPlanPayments = super.search(paymentSearch);

        for (SubscriptionPlanPayment payment : fetchedSubscriptionPlanPayments) {
            try {
                FlutterReponse flutterReponse = new FlutterwaveClient().checkPaymentStatusByTransactionId(payment.getTransactionId());

                payment.setLastPgwResponse(new Gson().toJson(flutterReponse));
                payment.setDateChanged(LocalDateTime.now());
                if (flutterReponse.status.equalsIgnoreCase("error")) {

                    payment.setStatus(TransactionStatus.FAILED);
                    saveInstance(payment);
                } else if (FlutterwaveTransactionStatus.SUCCESSFULL.getStatusName().equals(flutterReponse.data.status)) {

                    payment.setStatus(TransactionStatus.SUCESSFULL);
                    payment = saveInstance(payment);
                    try {
                        ApplicationContextProvider.getBean(MemberSubscriptionPlanService.class).activate(payment);

                        ApplicationContextProvider.getBean(NotificationService.class)
                                .sendNotificationsToMember(
                                        new NotificationBuilder()
                                                .setTitle("Payment Successfull")
                                                .setDescription("Your payment for " + payment.getSubscriptionPlan().getName() + " has been recieved")
                                                .setDestinationActivity(NotificationDestinationActivity.HOME)
                                                .setImageUrl(payment.getSubscriptionPlan().getImageUrl())
                                                .setDestinationInstanceId(String.valueOf(payment.getSubscriptionPlan().getId()))
                                                .build(), payment.getSubscriber(), true);

                    } catch (Exception ex) {
                        Logger.getLogger(SubscriptionPlanPaymentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean isDeletable(SubscriptionPlanPayment entity) throws OperationFailedException {
        return false;

    }

    @Override
    public SubscriptionPlanPayment initiatePayment(SubscriptionPlan subscriptionplan, Member member) throws IOException, OperationFailedException, ValidationFailedException {

        if (subscriptionplan == null) {
            throw new ValidationFailedException("SubscriptionPlan Not Found");
        }

        MemberSubscriptionPlan memberSubscriptionPlan = ApplicationContextProvider.getBean(MemberSubscriptionPlanService.class).getInstance(member, subscriptionplan);
        if (memberSubscriptionPlan != null) {
            throw new ValidationFailedException("You already purchased this subscriptionplan. Go to My-SubscriptionPlans to view your subscriptionplan.");
        }

        SystemSetting setting = ApplicationContextProvider.getBean(SystemSettingService.class).getAppSetting();
        if (setting == null || setting.getFlutterwaveEncryptionKey() == null || setting.getFlutterwavePublicKey() == null) {
            throw new ValidationFailedException("Rave settings not yet configured");

        }
        SubscriptionPlanPayment newPayment = new SubscriptionPlanPayment();
        newPayment.setSubscriber(member);
        newPayment.setSubscriptionPlan(subscriptionplan);

        //set currency and amounts
        newPayment.setCurrency(setting.getBaseCurrency());
        newPayment.setAmount(subscriptionplan.getCost());
        newPayment.setTitle("Payment For " + subscriptionplan.getName());

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
