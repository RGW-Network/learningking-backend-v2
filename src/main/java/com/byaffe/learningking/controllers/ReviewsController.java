package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.courses.ReviewRequestDTO;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.courses.Review;
import com.byaffe.learningking.models.courses.ReviewType;
import com.byaffe.learningking.services.CourseRatingService;
import com.byaffe.learningking.services.EventService;
import com.byaffe.learningking.services.impl.CourseRatingServiceImpl;
import com.byaffe.learningking.services.impl.EventServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/reviews")
public class ReviewsController {
    @Autowired
    CourseRatingService  ratingService;


    @PostMapping("")
    public ResponseEntity<ResponseObject<Review>> rateCourse(@RequestBody ReviewRequestDTO reviewRequestDTO) throws JSONException {

        return ResponseEntity.ok().body(new ResponseObject<>(ApplicationContextProvider.getBean(CourseRatingService.class).saveInstance(reviewRequestDTO)));
    }
    @PostMapping("/{id}/publish")
    public ResponseEntity<BaseResponse> publish(@PathVariable("id") Long id) throws JSONException {
      if(UserDetailsContext.isSuperAdmin()){
          ratingService.updateStatus(id, PublicationStatus.ACTIVE);
      }
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<BaseResponse> unPublish(@PathVariable("id") Long id) throws JSONException {
        if(UserDetailsContext.isSuperAdmin()){
            ratingService.updateStatus(id, PublicationStatus.INACTIVE);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/feature")
    public ResponseEntity<BaseResponse> feature(@PathVariable("id") Long id) throws JSONException {
        if(UserDetailsContext.isSuperAdmin()){
            ratingService.updateFeatured(id, true);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/unfeature")
    public ResponseEntity<BaseResponse> unFeature(@PathVariable("id") Long id) throws JSONException {
        if(UserDetailsContext.isSuperAdmin()){
            ratingService.updateFeatured(id, false);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));

    }

    @GetMapping("")
    public ResponseEntity<ResponseList<Review>> getRatings(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "type", required = false) ReviewType type,
            @RequestParam(value = "recordId", required = false) Long recordId,
            @RequestParam(value = "featured", required = false) Boolean featured,
            @RequestParam(value = "byMe", required = false) Boolean byMe,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit) throws JSONException {
        Search search = CourseRatingServiceImpl.generateSearchTermsForReviews(searchTerm);
        if (type != null) {
            search.addFilterEqual("type", type);
        }
        if (featured != null) {
            search.addFilterEqual("featured", featured);
        }
        if (recordId != null) {
            search.addFilterEqual("recordId", recordId);
        }
        if (byMe != null) {
            search.addFilterEqual("createdById", UserDetailsContext.getLoggedInUser().id);
        }
        if (UserDetailsContext.getLoggedInStudent()!=null) {
            search.addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
        }
        List<Review> reviews = ApplicationContextProvider.getBean(CourseRatingService.class).getInstances(search, offset, limit);
        long count = ApplicationContextProvider.getBean(CourseRatingService.class).countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(reviews, count, offset, limit));

    }





}
