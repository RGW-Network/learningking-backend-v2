package com.byaffe.learningking.controllers;

import com.byaffe.learningking.controllers.dtos.CourseCategoryRequestDTO;
import com.byaffe.learningking.dtos.LookupDTO;
import com.byaffe.learningking.dtos.LookupValueDTO;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.services.CourseCategoryService;
import com.byaffe.learningking.services.LookupValueService;
import com.byaffe.learningking.services.impl.CourseCategoryServiceImpl;
import com.byaffe.learningking.services.impl.LookupServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.models.Country;
import com.google.gson.Gson;
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
@RequestMapping("/api/v1/categories")
public class CategoriesController {

    @Autowired
    CourseCategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("")
    public ResponseEntity<ResponseList<CourseCategory>> getLookupValue(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                       @RequestParam("offset") int offset,
                                                                       @RequestParam("limit") int limit,
                                                                       @RequestParam(required = false, value = "commaSeparatedAcademyIds") String commaSeparatedAcademyIds,
                                                                       @RequestParam(required = false, value = "commaSeparatedTypeIds") String commaSeparatedTypeIds){
        Search search = CourseCategoryServiceImpl.composeSearchObject(searchTerm);
        if(StringUtils.isNotEmpty(commaSeparatedTypeIds)){
            String[] list = commaSeparatedTypeIds.split(",");
            List<CategoryType> lookupTypes= Arrays.stream(list).map(r->CategoryType.getById(Integer.parseInt(r))).collect(Collectors.toList());
            search.addFilterIn("type", lookupTypes);
        }
        if(StringUtils.isNotEmpty(commaSeparatedAcademyIds)){
            String[] list = commaSeparatedAcademyIds.split(",");
            List<CourseAcademyType> lookupTypes= Arrays.stream(list).map(r->CourseAcademyType.getById(Integer.parseInt(r))).collect(Collectors.toList());
            search.addFilterIn("academy", lookupTypes);
        }
        long totalRecords = categoryService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(categoryService.getInstances(search, offset, limit), totalRecords, offset, limit));

    }

    @PostMapping("")
    public ResponseEntity<ResponseObject<CourseCategory>> saveLookupValue(@RequestBody CourseCategoryRequestDTO LookupValueDTO) throws ValidationException {
        return ResponseEntity.ok().body(new ResponseObject<>( categoryService.saveInstance(LookupValueDTO)));
    }



}
