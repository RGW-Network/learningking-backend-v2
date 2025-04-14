package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.courses.CustomCourseRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.CourseCustomisationRequest;
import com.byaffe.learningking.models.courses.OrganisationGroup;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.CourseCustomisationRequestService;
import com.byaffe.learningking.services.impl.ArticleServiceImpl;
import com.byaffe.learningking.services.impl.CustomisationRequestServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("api/v1/tailor-a-course")
public class TailorCourseController {
    @Autowired
    ModelMapper modelMapper;
@Autowired
    CourseCustomisationRequestService courseCustomisationRequestService;
    @PostMapping(path = "")
    public ResponseEntity<ResponseObject<BaseResponse>> add(@RequestBody CustomCourseRequestDTO dto) {
courseCustomisationRequestService.save(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(new BaseResponse(true)));
    }

    @GetMapping("")
    public ResponseEntity<ResponseList<CourseCustomisationRequest>> get(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                                @RequestParam(value = "offset", required = true) Integer offset,
                                                                                @RequestParam(value = "limit", required = true) Integer limit,
                                                                                @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                                @RequestParam(value = "sortDescending", required = false) Boolean sortDescending) throws JSONException {

        Search search = CustomisationRequestServiceImpl.generateSearchObjectForArticles(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (StringUtils.isNotEmpty(sortBy)) {
            search.addSort(sortBy, sortDescending);
        }
        List<CourseCustomisationRequest> articles = courseCustomisationRequestService.getInstances(search, offset, limit);
        long count = courseCustomisationRequestService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(articles, (int) count, offset, limit));
    }




}
