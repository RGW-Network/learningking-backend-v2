package com.byaffe.learningking.controllers;

import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.services.SubscriptionPlanService;
import com.byaffe.learningking.services.impl.CategoryServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    @Autowired
    SubscriptionPlanService subscriptionPlanService;



    @GetMapping("")
    public ResponseEntity<ResponseList<SubscriptionPlan>> getPlans(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                         @RequestParam("offset") int offset,
                                                                         @RequestParam("limit") int limit){
        Search search = CategoryServiceImpl.composeSearchObject(searchTerm);
        search.addFilterIn("publicationStatus", PublicationStatus.ACTIVE);
        long totalRecords = subscriptionPlanService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(subscriptionPlanService.getInstances(search, offset, limit), totalRecords, offset, limit));
    }

    @PostMapping("{subscriptionPlanId}/purchase")
    public ResponseEntity<BaseResponse> save(@PathVariable(name = "subscriptionPlanId") Long subscriptionPlanId) throws ValidationFailedException  {
        return ResponseEntity.ok().body(new BaseResponse(true));
    }



}
