package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseRating;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.CourseRatingService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseRatingServiceImpl extends GenericServiceImpl<CourseRating> implements CourseRatingService {

  
    @Override
    public CourseRating saveInstance(CourseRating instance) throws ValidationFailedException, OperationFailedException {

        if (instance.getMember()==null) {
            throw new ValidationFailedException("Missing member");
        }
        if (instance.getCourse()==null) {
            throw new ValidationFailedException("Missing course");
        }

        if (instance.getStarsCount()<0) {
            throw new ValidationFailedException("Missing Stars Count");
        }

        return super.save(instance);

    }

    @Override
    public List<CourseRating> getInstances(Search arg0, int arg1, int arg2) {
    
        return super.getInstances(arg0, arg1, arg2); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int countInstances(Search arg0) {
    
        return super.countInstances(arg0); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
  
    }

    @Override
    public CourseRating activate(CourseRating plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        return super.save(plan);
    }

    @Override
    public CourseRating deActivate(CourseRating plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    @Override
    public List<CourseRating> getCourseRatings(Course course) {
        Search search = new Search();
        search.addFilterEqual("course", course);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.search(search);
    }

  

    @Override
    public boolean isDeletable(CourseRating entity) throws OperationFailedException {
        return true;
    }

    @Override
    public double getTotalCourseRatings(Course course) {
       return (double)super.searchUnique(new Search().addFilterEqual("course", course)
                 .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                  .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
               .addField("starsCount", Field.OP_SUM)); 
    }
    
    @Override
     public int getRatingsCount(Course course) {
       return super.count(new Search().addFilterEqual("course", course)
                 .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                  .addFilterEqual("recordStatus", RecordStatus.ACTIVE)); 
    }
}
