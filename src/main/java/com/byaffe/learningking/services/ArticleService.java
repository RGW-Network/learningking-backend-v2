package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface ArticleService  extends GenericService<Article> {

    


    Article activate(Article plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     */
    Article deActivate(Article plan);

    /**
     *
     * @param planTitle
     * @return
     */
    Article getByTitle(String planTitle);
    

}
