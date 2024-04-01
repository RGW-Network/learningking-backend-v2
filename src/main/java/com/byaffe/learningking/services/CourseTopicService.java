package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.CourseTopicRequestDTO;
import com.byaffe.learningking.models.courses.CourseLesson;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.CourseTopic;

public interface CourseTopicService extends GenericService<CourseTopic> {
    
     public CourseTopic getFirstTopic(CourseLesson courseLesson);
    public CourseTopic saveInstance(CourseTopicRequestDTO dto);
     
      public float getProgress(CourseLecture currentSubTopic);
   
}
