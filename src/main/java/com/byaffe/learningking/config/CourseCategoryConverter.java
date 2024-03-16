package com.byaffe.learningking.config;

import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.services.CourseCategoryService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import org.springframework.core.convert.converter.Converter;


public class CourseCategoryConverter implements Converter<Long, CourseCategory> {

    @Override
    public CourseCategory convert(Long from) {
        return ApplicationContextProvider.getBean(CourseCategoryService.class).getInstanceByID(from);
    }
}