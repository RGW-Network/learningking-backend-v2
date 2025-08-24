package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.ReviewRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.util.List;

;

/**
 * Responsible for CRUD operations on {@link Category}
 *
 * @author RayGdhrt
 *
 */
public interface CourseRatingService  extends GenericService<Review> {

    public Review saveInstance(ReviewRequestDTO dto);
    public Review updateStatus(long id, PublicationStatus publicationStatus);

    public Review updateFeatured(long id,boolean isFeatured);
    
     public List<Review> getCourseRatings(ReviewType type, Long recordId);
     
      public double getTotalCourseRatings(ReviewType type, Long recordId);
      public int getRatingsCount(ReviewType type, Long recordId);
   

}
