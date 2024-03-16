package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CourseLessonService;
import com.byaffe.learningking.services.CourseSubTopicService;
import com.byaffe.learningking.services.CourseTopicService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CourseSubTopicServiceImpl
        extends GenericServiceImpl<CourseLecture> implements CourseSubTopicService {

    @Autowired
    CourseTopicService courseTopicService;
    @Autowired
    CourseLessonService courseLessonService;

    @Override
    public boolean isDeletable(CourseLecture entity) throws OperationFailedException {
        return true;
    }

    @Override
    public CourseLecture saveInstance(CourseLecture instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
    }

    @Override
    public CourseLecture getFirstSubTopic(CourseLesson courseLesson) {
        CourseTopic firstCourseTopic = courseTopicService.getFirstTopic(courseLesson);
        return getFirstSubTopic(firstCourseTopic);
    }

    @Override
    public CourseLecture getFirstSubTopic(CourseTopic courseTopic) {
        return super.searchUnique(new Search()
                .addFilterEqual("courseTopic", courseTopic)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addSortAsc("position")
                .setFirstResult(0)
                .setMaxResults(1));

    }

    @Override
    public CourseLecture getFirstSubTopic(Course course) {
        CourseLesson firstCourseLesson = courseLessonService.getFirstLesson(course);
        CourseTopic firstCourseTopic = courseTopicService.getFirstTopic(firstCourseLesson);
        return getFirstSubTopic(firstCourseTopic);

    }

}
