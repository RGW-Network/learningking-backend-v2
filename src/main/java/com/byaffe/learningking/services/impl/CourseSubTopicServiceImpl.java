package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.CourseLessonDao;
import com.byaffe.learningking.dtos.courses.LectureRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CourseLessonService;
import com.byaffe.learningking.services.CourseSubTopicService;
import com.byaffe.learningking.services.CourseTopicService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CourseSubTopicServiceImpl
        extends GenericServiceImpl<CourseLecture> implements CourseSubTopicService {

    @Autowired
    CourseTopicService courseTopicService;
    @Autowired
    CourseLessonService courseLessonService;
    @Autowired
    CourseLessonDao courseLessonDao;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ImageStorageService imageStorageService;
    @Override
    public boolean isDeletable(CourseLecture entity) throws OperationFailedException {
        return true;
    }

    @Override
    public CourseLecture saveInstance(CourseLecture instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
    }

    @Override
    public CourseLecture saveInstance(LectureRequestDTO dto) {

        CourseLecture courseLesson= modelMapper.map(dto,CourseLecture.class);
        CourseTopic course= courseTopicService.getInstanceByID(dto.getCourseTopicId());
        courseLesson.setCourseTopic(course);
        if (course == null) {
            throw new ValidationFailedException("Missing lesson");
        }
        courseLesson= super.save(courseLesson);
        if(ObjectUtils.allNotNull( dto.getCoverImage())) {
            String imageUrl=   imageStorageService.uploadImage(dto.getCoverImage(), "course-lectures/" + course.getId());
            courseLesson.setCoverImageUrl(imageUrl);
            courseLesson=super.save(courseLesson);
        }
        return courseLesson;
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
