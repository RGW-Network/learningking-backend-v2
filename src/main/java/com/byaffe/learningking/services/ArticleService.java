package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface ArticleService  extends GenericService<Article> {


    Article save(ArticleRequestDTO dto) throws ValidationFailedException;


    Article activate(long plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     */
    Article deActivate(long plan);

    /**
     *
     * @param planTitle
     * @return
     */
    Article getByTitle(String planTitle);
    

}
