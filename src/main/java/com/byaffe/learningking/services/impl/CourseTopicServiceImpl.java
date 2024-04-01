package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.CourseLectureDao;
import com.byaffe.learningking.daos.CourseLessonDao;
import com.byaffe.learningking.dtos.courses.CourseTopicRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CourseTopicService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseTopicServiceImpl extends BaseDAOImpl<CourseTopic> implements CourseTopicService {

    @Autowired
    CourseLectureDao courseLectureDao;

    @Autowired
    CourseLessonDao courseLessonDao;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public CourseTopic saveInstance(CourseTopicRequestDTO dto) {

        CourseTopic courseLesson= modelMapper.map(dto,CourseTopic.class);
        CourseLesson course= courseLessonDao.getReference(dto.getCourseLessonId());
        courseLesson.setCourseLesson(course);
        if (course == null) {
            throw new ValidationFailedException("Missing lesson");
        }
        return super.save(courseLesson);
    }
    @Override
    public CourseTopic saveInstance(CourseTopic seriesPart) throws ValidationFailedException {

        if (seriesPart.getCourseLesson() == null) {
            throw new ValidationFailedException("Missing empowerment serie");
        }

        if (seriesPart.getPosition() == 0) {
            int seriesInPart = countInstances(new Search().addFilterEqual("courseLesson", seriesPart.getCourseLesson()).addFilterEqual("recordStatus", RecordStatus.ACTIVE));
            seriesPart.setPosition(seriesInPart + 1);
        }

        return super.merge(seriesPart);

    }

    @Override
    public float getProgress(CourseLecture currentSubTopic) {
        if (currentSubTopic==null) {
            return 0;
        }
        List<CourseLecture> allSubTopics = courseLectureDao.search(new Search()
                 .addSortAsc("position")
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseTopic", currentSubTopic.getCourseTopic()));
if (allSubTopics.isEmpty()) {
            return 0;
        }
        int currentPosition = allSubTopics.indexOf(currentSubTopic) + 1;//the +1 caters for zero based indexing
        return (float) (currentPosition * 100) / allSubTopics.size();

    }

    @Override
    public List<CourseTopic> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        search.addSortAsc("position");
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.search(search);
    }

    public int countInstances(Search search) {
        if (search == null) {
            search = new Search();
        }
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.count(search);
    }

    @Override
    public CourseTopic getInstanceByID(Long id) {
        return super.searchUniqueByPropertyEqual("id", id, RecordStatus.ACTIVE);
    }

    public CourseTopic getFirstTopic(CourseLesson courseLesson) {
        return super.searchUnique(new Search()
                .addFilterEqual("courseLesson", courseLesson)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addSortAsc("position")
                .setFirstResult(0)
                .setMaxResults(1));

    }



    @Override
    public void deleteInstance(CourseTopic instance) throws OperationFailedException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteInstances(Search search) throws OperationFailedException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
