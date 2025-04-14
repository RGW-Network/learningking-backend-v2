package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.dtos.courses.CustomCourseRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.SubmissionStatus;
import com.byaffe.learningking.models.courses.CourseCustomisationRequest;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class CustomisationRequestServiceImpl extends GenericServiceImpl<CourseCustomisationRequest> implements CourseCustomisationRequestService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryService categoryService;

    @Autowired
    InstructorService instructorService;

    public static Search generateSearchObjectForArticles(String searchTerm) {

   return  new Search();
    }



    @Override
    public CourseCustomisationRequest save(CustomCourseRequestDTO dto) throws ValidationFailedException {
        if (dto.getCourseCategoryId()== null) {
            throw new ValidationFailedException("Missing category");
        }

        if (StringUtils.isBlank(dto.getCourseDetails())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(dto.getExpectedOutComes())) {
            throw new ValidationFailedException("Missing Description");
        }

        CourseCustomisationRequest article=modelMapper.map(dto,CourseCustomisationRequest.class);
        article.setCourseCategory(categoryService.getInstanceByID(dto.getCourseCategoryId()));
        article= saveInstance(article);
        return article;
    }

    @Override
    public CourseCustomisationRequest activate(long plan) throws ValidationFailedException {
        CourseCustomisationRequest article=getInstanceByID(plan);
        article.setStatus(SubmissionStatus.Resolved);

        CourseCustomisationRequest savedDevotionPlan = super.save(article);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllStudents(
                    new NotificationBuilder()
                            .setTitle("Your submission request was approved")
                            .setDescription(article.getCourseDetails())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(article.getId()))
                            .build());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return savedDevotionPlan;
    }

    @Override
    public CourseCustomisationRequest deActivate(long id) {
        CourseCustomisationRequest plan=getInstanceByID(id);
        plan.setStatus(SubmissionStatus.Pending);
        return super.save(plan);
    }

    @Override
    public CourseCustomisationRequest getByTitle(String planTitle) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("title", planTitle);

        return super.searchUnique(search);

    }


    public static Search generateSearchTermsForArticles(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("title", "description"));
        return search;
    }


    @Override
    public boolean isDeletable(CourseCustomisationRequest entity) throws OperationFailedException {
        return true;
    }

    @Override
    public CourseCustomisationRequest saveInstance(CourseCustomisationRequest instance) throws ValidationFailedException, OperationFailedException {
        return null;
    }
}
