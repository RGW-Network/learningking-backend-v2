package com.byaffe.learningking.controllers;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.constants.TransactionType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.services.PaymentService;
import com.byaffe.learningking.services.SubscriptionPlanService;
import com.byaffe.learningking.services.impl.CategoryServiceImpl;
import com.byaffe.learningking.services.impl.PaymentServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    @Autowired
    SubscriptionPlanService subscriptionPlanService;

    @Autowired
    PaymentService paymentService;

    @GetMapping("/subscription-plans")
    public ResponseEntity<ResponseList<SubscriptionPlan>> getPlans(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                   @RequestParam("offset") int offset,
                                                                   @RequestParam("limit") int limit) {
        Search search = CategoryServiceImpl.composeSearchObject(searchTerm);
        search.addFilterIn("publicationStatus", PublicationStatus.ACTIVE);
        long totalRecords = subscriptionPlanService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(subscriptionPlanService.getInstances(search, offset, limit), totalRecords, offset, limit));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<AggregatorTransaction>> getPayments(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                   @RequestParam("offset") int offset,
                                                                   @RequestParam("limit") int limit) {
        Search search = PaymentServiceImpl.composeSearchObject(searchTerm);
        if(!Objects.requireNonNull(UserDetailsContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("student.id", Objects.requireNonNull(UserDetailsContext.getLoggedInStudent()).getId());
            search.addFilterEqual("status", TransactionStatus.SUCCESSFUL);
        }
        long totalRecords = paymentService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(paymentService.getInstances(search, offset, limit), totalRecords, offset, limit));
    }

    @PostMapping("/pay/{type}/{recordId}")
    public ResponseEntity<ResponseObject<AggregatorTransaction>> save(@PathVariable(name = "type", required = true) TransactionType type, @PathVariable(name = "recordId", required = true) Long recordId) throws ValidationFailedException, IOException {
        AggregatorTransaction response = null;
        if (type.equals(TransactionType.COURSE_PAYMENT)) {
            response = paymentService.initiateCoursePayment(recordId, Objects.requireNonNull(UserDetailsContext.getLoggedInStudent()).getId());
        } else if (type.equals(TransactionType.SUBSCRIPTION_PAYMENT)) {
            response = paymentService.initiateSubscriptionPlanPayment(recordId, Objects.requireNonNull(UserDetailsContext.getLoggedInStudent()).getId());
        } else if (type.equals(TransactionType.EVENT_PAYMENT)) {
            response = paymentService.initiateEventPayment(recordId, Objects.requireNonNull(UserDetailsContext.getLoggedInStudent()).getId());
        }

        return ResponseEntity.ok().body(new ResponseObject<>(response));
    }
}




