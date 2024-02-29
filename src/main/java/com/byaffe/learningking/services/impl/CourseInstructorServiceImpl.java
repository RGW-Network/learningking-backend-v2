package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.services.CourseInstructorService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CourseInstructorServiceImpl
        extends GenericServiceImpl<CourseInstructor> implements CourseInstructorService {

    @Override
    public boolean isDeletable(CourseInstructor entity) throws OperationFailedException {
        return true;
    }

    @Override
    public CourseInstructor saveInstance(CourseInstructor instance) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isBlank(instance.getFullName())) {
            throw new ValidationFailedException("Missing name");
        }
        return super.save(instance);
    }

   


}
