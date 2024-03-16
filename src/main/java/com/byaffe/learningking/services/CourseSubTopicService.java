package com.byaffe.learningking.services;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.CourseTopic;

public interface CourseSubTopicService extends GenericService<CourseLecture> {

    CourseLecture getFirstSubTopic(CourseLesson courseLesson);
    
    CourseLecture getFirstSubTopic(CourseTopic courseTopic);
    
    CourseLecture getFirstSubTopic(Course course);
    
}
