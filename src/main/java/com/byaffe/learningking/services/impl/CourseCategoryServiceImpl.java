package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.controllers.dtos.CourseCategoryRequestDTO;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.services.CourseCategoryService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class CourseCategoryServiceImpl
        extends GenericServiceImpl<CourseCategory> implements CourseCategoryService {
@Autowired
    ModelMapper modelMapper;
    @Override
    public boolean isDeletable(CourseCategory entity) throws OperationFailedException {
        return true;
    }

    @Override
    public CourseCategory saveInstance(CourseCategory instance) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isBlank(instance.getName())) {
            throw new ValidationFailedException("Missing name");
        }

        if (instance.getAcademy() == null) {
            throw new ValidationFailedException("Missing academy");
        }
        CourseCategory existsWithType = getCategoryByNameAndAcademy(instance.getName(), CourseAcademyType.PROFFESSIONAL);
        if (existsWithType != null && !existsWithType.getId().equals(instance.getId())) {
            throw new ValidationFailedException("Category With Same name and type exists");
        }

        return super.save(instance);
    }

    public CourseCategory saveInstance(CourseCategoryRequestDTO dto) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isBlank(dto.getName())) {
            throw new ValidationFailedException("Missing name");
        }

        if (dto.getAcademy() == null) {
            throw new ValidationFailedException("Missing academy");
        }
        CourseCategory existsWithType = getCategoryByNameAndAcademy(dto.getName(), CourseAcademyType.PROFFESSIONAL);
        if (existsWithType != null && !existsWithType.getId().equals(dto.getId())) {
            throw new ValidationFailedException("Category With Same name and type exists");
        }

        CourseCategory courseCategory=modelMapper.map(dto,CourseCategory.class);
        return super.save(courseCategory);
    }

    @Override
    public CourseCategory getCategoryByAcademy(CourseAcademyType courseType) {
        return super.searchUniqueByPropertyEqual("academy", courseType, RecordStatus.ACTIVE);
    }

    
    @Override
    public CourseCategory getCategoryByNameAndAcademy(String name, CourseAcademyType courseType) {
        Search search = new Search().addFilterEqual("name", name)
                .addFilterEqual("academy", courseType)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.searchUnique(search);
    }

    public static Search composeSearchObject(String searchTerm) {
        com.googlecode.genericdao.search.Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("name","description"));

        return search;
    }
}
