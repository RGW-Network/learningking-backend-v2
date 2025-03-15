package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.PasswordResetToken;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface PasswordResetService extends GenericService<PasswordResetToken> {

    public void initiatePasswordReset(String email);



    public void resetPassword(String token, String newPassword);

}
