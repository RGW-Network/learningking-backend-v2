package com.byaffe.learningking.controllers;

import com.byaffe.learningking.controllers.dtos.CourseCategoryRequestDTO;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.services.CategoryService;
import com.byaffe.learningking.services.impl.CategoryServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
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
    CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("")
    public ResponseEntity<ResponseList<Category>> getLookupValue(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                 @RequestParam("offset") int offset,
                                                                 @RequestParam("limit") int limit,
                                                                 @RequestParam(required = false, value = "commaSeparatedTypes") String commaSeparatedTypes){
        Search search = CategoryServiceImpl.composeSearchObject(searchTerm);
        if(StringUtils.isNotEmpty(commaSeparatedTypes)){
            String[] list = commaSeparatedTypes.split(",");
            List<CategoryType> lookupTypes= Arrays.stream(list).map(CategoryType::valueOf).collect(Collectors.toList());
            search.addFilterIn("type", lookupTypes);
        }

        long totalRecords = categoryService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(categoryService.getInstances(search, offset, limit), totalRecords, offset, limit));

    }

    @PostMapping("")
    public ResponseEntity<ResponseObject<Category>> saveLookupValue(@RequestBody CourseCategoryRequestDTO courseCategoryRequestDTO) throws ValidationException {
        return ResponseEntity.ok().body(new ResponseObject<>( categoryService.saveInstance(courseCategoryRequestDTO)));
    }



}
