package com.byaffe.learningking.controllers;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.constants.TransactionType;
import com.byaffe.learningking.dtos.BulkPaymentRequestDTO;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.models.payments.StudentSubscriptionPlan;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.services.CourseService;
import com.byaffe.learningking.services.PaymentService;
import com.byaffe.learningking.services.StudentSubscriptionPlanService;
import com.byaffe.learningking.services.SubscriptionPlanService;
import com.byaffe.learningking.services.impl.CategoryServiceImpl;
import com.byaffe.learningking.services.impl.PaymentServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
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
public class  PaymentsController {

    @Autowired
    SubscriptionPlanService subscriptionPlanService;

    @Autowired
    StudentSubscriptionPlanService studentSubscriptionPlanService;

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
        if(!Objects.requireNonNull(SessionContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("student.id", Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId());
            search.addFilterEqual("status", TransactionStatus.SUCCESSFUL);
        }
        long totalRecords = paymentService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(paymentService.getInstances(search, offset, limit), totalRecords, offset, limit));
    }

    @PostMapping("/pay/{type}/{recordId}")
    public ResponseEntity<ResponseObject<AggregatorTransaction>> save(@PathVariable(name = "type", required = true) TransactionType type, @PathVariable(name = "recordId", required = true) Long recordId, @RequestBody(required = false) BulkPaymentRequestDTO dto) throws ValidationFailedException, IOException {
        AggregatorTransaction response = null;
        if (type.equals(TransactionType.COURSE_PAYMENT)) {
            response = paymentService.initiateCoursePayment(recordId, Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId());
        } else if (type.equals(TransactionType.SUBSCRIPTION_PAYMENT)) {
            response = paymentService.initiateSubscriptionPlanPayment(recordId, Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId());
        } else if (type.equals(TransactionType.EVENT_PAYMENT)) {
            response = paymentService.initiateEventPayment(recordId, Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId());
        }
        else if (type.equals(TransactionType.BULK_EVENT_PAYMENT)) {
            if(dto==null||dto.getOrganisationGroupId()==null) throw  new ValidationFailedException("Missing Group Id for bulk purchase.");
            response = paymentService.initiateBulkEventPayment(recordId, Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId(),dto.getOrganisationGroupId());
        }
        else if (type.equals(TransactionType.BULK_SUBSCRIPTION_PAYMENT)) {
            if(dto==null||dto.getOrganisationGroupId()==null) throw  new ValidationFailedException("Missing Group Id for bulk purchase.");
            response = paymentService.initiateBulkSubscriptionPlanPayment(recordId, Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId(),dto.getOrganisationGroupId());
        }
        else if (type.equals(TransactionType.BULK_COURSE_PAYMENT)) {
            if(dto==null||dto.getOrganisationGroupId()==null) throw  new ValidationFailedException("Missing Group Id for bulk purchase.");
            response = paymentService.initiateBulkCoursePayment(recordId, Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId(),dto.getOrganisationGroupId());
        }

        return ResponseEntity.ok().body(new ResponseObject<>(response));
    }

    @PostMapping("/pay-by-subscription/{courseId}/{subscriptionId}")
    public ResponseEntity<ResponseObject<BaseResponse>> payWithSubscription(@PathVariable(name = "courseId", required = true) Long courseId, @PathVariable(name = "subscriptionId", required = true) Long subscriptionId) throws ValidationFailedException, IOException {

        Course course= ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(courseId);
        StudentSubscriptionPlan plan= studentSubscriptionPlanService.getInstanceByID(subscriptionId);
        studentSubscriptionPlanService.payBySubscription(course,plan);
        return ResponseEntity.ok().body(new ResponseObject<>(new BaseResponse()));
    }

    @GetMapping("/my-subscriptions")
    public ResponseEntity<ResponseList<StudentSubscriptionPlan>> getMySubscriptions(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                           @RequestParam("offset") int offset,
                                                                           @RequestParam("limit") int limit) {
        Search search = new Search();
        if(!Objects.requireNonNull(SessionContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("student.id", Objects.requireNonNull(SessionContext.getLoggedInStudent()).getId());
             }
        long totalRecords = studentSubscriptionPlanService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(studentSubscriptionPlanService.getInstances(search, offset, limit), totalRecords, offset, limit));
    }
}




