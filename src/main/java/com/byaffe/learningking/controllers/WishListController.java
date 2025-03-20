package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.EventResponseDto;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.WishList;
import com.byaffe.learningking.services.EventAttendanceService;
import com.byaffe.learningking.services.EventService;
import com.byaffe.learningking.services.WishListService;
import com.byaffe.learningking.services.impl.EventServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/wishlist")
public class WishListController {
    @Autowired
    ModelMapper modelMapper;
    
@Autowired
    WishListService  wishListService;
    @PostMapping("/add/{courseId}")
    public ResponseEntity<ResponseObject<WishList>> publishEvent(@PathVariable(name = "courseId", required = true) long courseId) throws JSONException {
      WishList wishList=  wishListService.addToWishList(courseId);
        return ResponseEntity.ok().body(new ResponseObject<>(wishList));
    }
    @PostMapping("/remove/{courseId}")
    public ResponseEntity<BaseResponse> remove(@PathVariable(name = "courseId") long courseId) throws JSONException {
        wishListService.removeCourseFromWishList(courseId);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/clear")
    public ResponseEntity<BaseResponse> clear() throws JSONException {
        wishListService.clearWishList();
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<WishList>> getWishList(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                            @RequestParam(value = "limit", required = true) Integer limit,
                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                            @RequestParam(value = "courseId", required = false) Long courseId) throws JSONException {

        Search search = EventServiceImpl.generateSearchTermsForEvents(searchTerm);
        if (!Objects.requireNonNull(SessionContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("student", SessionContext.getLoggedInStudent());
        }
        if (courseId!=null) {
            search.addFilterEqual("course.id", courseId);
        }
        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        List<WishList> records = wishListService.getInstances(search, offset, limit);
        long count = wishListService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(records, (int) count, offset, limit));
    }




}
