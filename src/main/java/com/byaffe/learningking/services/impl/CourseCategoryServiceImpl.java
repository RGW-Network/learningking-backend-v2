package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.services.CourseCategoryService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CourseCategoryServiceImpl
        extends GenericServiceImpl<CourseCategory> implements CourseCategoryService {

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
    

}
