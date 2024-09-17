package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.SubscriptionPlanRequestDTO;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.payments.SubscriptionPlan;
import com.byaffe.learningking.services.SubscriptionPlanService;
import com.byaffe.learningking.services.impl.CategoryServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/subscription-plans")
public class AdminSubscriptionPlansController {

    @Autowired
    SubscriptionPlanService subscriptionPlanService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("")
    public ResponseEntity<ResponseList<SubscriptionPlan>> getList(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                         @RequestParam("offset") int offset,
                                                                         @RequestParam("limit") int limit,
                                                                         @RequestParam(required = false, value = "commaSeparatedTypes") String commaSeparatedTypes,
                                                                         @RequestParam(required = false, value = "commaSeparatedAcademies") String commaSeparatedAcademies){
        Search search = CategoryServiceImpl.composeSearchObject(searchTerm);
        if(StringUtils.isNotEmpty(commaSeparatedTypes)){
            String[] list = commaSeparatedTypes.split(",");
            List<CategoryType> lookupTypes= Arrays.stream(list).map(CategoryType::valueOf).collect(Collectors.toList());
            search.addFilterIn("type", lookupTypes);
        }
        if(StringUtils.isNotEmpty(commaSeparatedAcademies)){
            String[] list = commaSeparatedAcademies.split(",");
            List<CourseAcademyType> lookupTypes= Arrays.stream(list).map(CourseAcademyType::valueOf).collect(Collectors.toList());
            search.addFilterIn("academy", lookupTypes);
        }

        long totalRecords = subscriptionPlanService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(subscriptionPlanService.getInstances(search, offset, limit), totalRecords, offset, limit));

    }

    @PostMapping("")
    public ResponseEntity<ResponseObject<SubscriptionPlan>> save(@RequestBody SubscriptionPlanRequestDTO dto) throws ValidationFailedException  {
        return ResponseEntity.ok().body(new ResponseObject<>( subscriptionPlanService.saveInstance(dto)));
    }



}
