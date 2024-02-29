package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.CourseLessonService;
import com.byaffe.learningking.services.CourseSubTopicService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseLessonServiceImpl extends GenericServiceImpl<CourseLesson> implements CourseLessonService {

    @Autowired
    CourseSubTopicService courseSubTopicService;

    @Override
    public CourseLesson saveInstance(CourseLesson seriesPart) throws ValidationFailedException {

        if (seriesPart.getCourse() == null) {
            throw new ValidationFailedException("Missing empowerment serie");
        }

        if (seriesPart.getPosition() == 0) {
            int seriesInPart = countInstances(new Search().addFilterEqual("empowermentSerie", seriesPart.getCourse()).addFilterEqual("recordStatus", RecordStatus.ACTIVE));
            seriesPart.setPosition(seriesInPart + 1);
        }

        return super.merge(seriesPart);

    }

    @Override
    public float getProgress(CourseSubTopic currentSubTopic) {
        
        if(currentSubTopic==null){
        return 0;
        
        }
        List<CourseSubTopic> allSubTopics = courseSubTopicService.getInstances(new Search()
                .addSortAsc("position")
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseTopic.courseLesson", currentSubTopic.getCourseTopic().getCourseLesson()), 0, 0);
        if (allSubTopics.isEmpty()) {
            return 0;
        }
        int currentPosition = allSubTopics.indexOf(currentSubTopic) + 1;//the +1 caters for zero based indexing
        return currentPosition * 100 / allSubTopics.size();

    }

    @Override
    public List<CourseLesson> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        search.addSortAsc("position");
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.search(search);
    }

    @Override
    public int countInstances(Search search) {
        if (search == null) {
            search = new Search();
        }
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.countInstances(search);
    }

    @Override
    public CourseLesson getInstanceByID(Long id) {
        return super.searchUniqueByPropertyEqual("id", id, RecordStatus.ACTIVE);
    }

    public CourseLesson getFirstLesson(Course course) {
        return super.searchUnique(new Search()
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addSortAsc("position")
                .setFirstResult(0)
                .setMaxResults(1));

    }

    @Override
    public boolean isDeletable(CourseLesson entity) throws OperationFailedException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
