package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.courses.ReviewRequestDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Review;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.courses.ReviewType;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CourseRatingServiceImpl extends GenericServiceImpl<Review> implements CourseRatingService {
    @Autowired
    CourseService courseService;


    @Override
    public Review saveInstance(Review instance) throws ValidationFailedException, OperationFailedException {


        return super.save(instance);

    }

    public Review saveInstance(ReviewRequestDTO dto) {
        if (!dto.getType().equals(ReviewType.GENERAL) && dto.getRecordId() == null) {
            throw new ValidationFailedException("Missing record Id");
        }
        if (dto.getStarsCount() < 0) {
            throw new ValidationFailedException("Missing Stars Count");
        }
        if (StringUtils.isEmpty(dto.getComment())) {
            throw new ValidationFailedException("Missing Comment");
        }
        Review review = new Review();
        if(dto.getId()!=null){
            review=getInstanceByID(dto.getId());
        }

        review.setComment(dto.getComment());
        review.setStarsCount(dto.getStarsCount());
        review.setType(dto.getType());
        review.setRecordId(dto.getRecordId());
        review.setRecordName(generateReferenceRecordName(dto.getType(), dto.getRecordId()));
        Student student = SessionContext.getLoggedInStudent();
        if (student != null) {
            review.setReviewerName(student.getFullName());
            review.setReviewerDesignation("Learningking Student");
            review.setReviewerImageUrl(student.getProfileImageUrl());
            if (student.getProfession() != null) {
                review.setReviewerDesignation(student.getProfession().getValue());
            }
        }

        return super.save(review);

    }

    private String generateReferenceRecordName(ReviewType type, Long recordId){
        if(type.equals(ReviewType.GENERAL)) return  null;
        if(recordId==null ||recordId<=0) throw  new ValidationFailedException("Invalid record Id");
        if (type.equals(ReviewType.COURSE)){
            return ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(recordId).getTitle();
        }
        if (type.equals(ReviewType.CERTIFICATION)){
            return ApplicationContextProvider.getBean(CertificationService.class).getInstanceByID(recordId).getTitle();
        }
        if (type.equals(ReviewType.ARTICLE)){
            return ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(recordId).getTitle();
        }
        if (type.equals(ReviewType.EVENT)){
            return ApplicationContextProvider.getBean(EventService.class).getInstanceByID(recordId).getTitle();
        }
        return null;
    }

    @Override
    public Review updateStatus(long id, PublicationStatus publicationStatus) {
        Review review= getInstanceByID(id);
        review.setPublicationStatus(publicationStatus);
        return super.save(review);
    }


    @Override
    public Review updateFeatured(long id, boolean isFeatured) {
        Review review= getInstanceByID(id);
        review.setFeatured(isFeatured);
        return super.save(review);
    }

    @Override
    public List<Review> getInstances(Search arg0, int arg1, int arg2) {

        return super.getInstances(arg0, arg1, arg2); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int countInstances(Search arg0) {

        return super.countInstances(arg0); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

    }


    @Override
    public List<Review> getCourseRatings(ReviewType type, Long recordId) {
        Search search = new Search();
        search.addFilterEqual("recordId", recordId).addFilterEqual("type", type);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.search(search);
    }


    @Override
    public boolean isDeletable(Review entity) throws OperationFailedException {
        return true;
    }

    @Override
    public double getTotalCourseRatings(ReviewType type, Long recordId) {
        Object ratings = super.searchUnique(new Search().addFilterEqual("recordId", recordId).addFilterEqual("type", type)
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addField("starsCount", Field.OP_SUM));
        if (ratings == null) {
            return 0.0;
        }
        return (double) ratings;

    }

    @Override
    public int getRatingsCount(ReviewType type, Long recordId) {
        return super.count(new Search().addFilterEqual("recordId", recordId).addFilterEqual("type", type)
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE));
    }


    public static Search generateSearchTermsForReviews(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("comment", "reviewerName",  "reviewerDesignation"));

        return search;
    }
}
