package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.dtos.courses.CustomCourseRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.courses.CourseCustomisationRequest;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface CourseCustomisationRequestService extends GenericService<CourseCustomisationRequest> {


    CourseCustomisationRequest save(CustomCourseRequestDTO dto) throws ValidationFailedException;


    CourseCustomisationRequest activate(long plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     */
    CourseCustomisationRequest deActivate(long plan);

    /**
     *
     * @param planTitle
     * @return
     */
    CourseCustomisationRequest getByTitle(String planTitle);
    

}
