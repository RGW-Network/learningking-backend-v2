package com.byaffe.learningking.services;

import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.models.courses.CourseTopic;

public interface CourseSubTopicService extends GenericService<CourseSubTopic> {

    CourseSubTopic getFirstSubTopic(CourseLesson courseLesson);
    
    CourseSubTopic getFirstSubTopic(CourseTopic courseTopic);
    
    CourseSubTopic getFirstSubTopic(Course course);
    
}
