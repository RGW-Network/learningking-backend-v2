package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.courses.CourseCategoryRequestDTO;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.services.CategoryService;
import com.byaffe.learningking.services.impl.CategoryServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    public ResponseEntity<ResponseList<Category>> getCategories(@RequestParam(required = false, value = "searchTerm") String searchTerm,
                                                                 @RequestParam("offset") int offset,
                                                                 @RequestParam("limit") int limit,
                                                                 @RequestParam(required = false, value = "isFeatured") Boolean isFeatured,
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
        if(isFeatured!=null){
          search.addFilterEqual("isFeatured", isFeatured);
        }

        long totalRecords = categoryService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(categoryService.getInstances(search, offset, limit), totalRecords, offset, limit));

    }

    @PostMapping(path = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})

    public ResponseEntity<ResponseObject<Category>> saveLookupValue(@RequestPart @Valid CourseCategoryRequestDTO dto
    ,@RequestPart(value = "icon",required = false) MultipartFile icon,
    @RequestPart(value = "image",required = false) MultipartFile image
    ) throws ValidationException {
        dto.setIcon(icon);
        dto.setImage(image);
        return ResponseEntity.ok().body(new ResponseObject<>( categoryService.saveInstance(dto)));
    }



}
