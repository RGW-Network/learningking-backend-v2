package com.byaffe.learningking.services;

import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.CourseTopic;

public interface CourseTopicService extends GenericService<CourseTopic> {
    
     public CourseTopic getFirstTopic(CourseLesson courseLesson);
     
      public float getProgress(CourseLecture currentSubTopic);
   
}
