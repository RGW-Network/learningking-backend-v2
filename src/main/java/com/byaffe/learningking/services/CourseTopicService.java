package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseSubTopic;
import com.byaffe.learningking.models.courses.CourseTopic;

public interface CourseTopicService extends GenericService<CourseTopic> {
    
     public CourseTopic getFirstTopic(CourseLesson courseLesson);
     
      public float getProgress(CourseSubTopic currentSubTopic);
   
}
