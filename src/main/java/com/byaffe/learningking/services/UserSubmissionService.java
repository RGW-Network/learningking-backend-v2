package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.UserSubmissionRequestDto;
import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.UserSubmission;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface UserSubmissionService extends GenericService<UserSubmission> {


    UserSubmission save(UserSubmissionRequestDto dto) throws ValidationFailedException;


    UserSubmission resolve(long plan) throws ValidationFailedException;

    

}
