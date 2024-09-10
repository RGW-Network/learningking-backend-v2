package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.courses.CourseRequestDTO;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class CourseServiceImpl extends GenericServiceImpl<Course> implements CourseService {

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CourseLectureService courseLectureService;
    @Autowired
    InstructorService instructorService;

    public static Search generateSearchObjectForCourses(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("title", "description"));

        return search;
    }

    @Override
    public Course saveInstance(CourseRequestDTO plan) throws ValidationFailedException {

        if (plan.getInstructorId() == null) {
            throw new ValidationFailedException("Instructor is required");
        }
        if (StringUtils.isBlank(plan.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(plan.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }
        Course course = modelMapper.map(plan, Course.class);
        course.setCategory(categoryService.getInstanceByID(plan.getCategoryId()));
        course.setCommaSeparatedTags(plan.getCommaSeparatedTags());
        course.setInstructor(instructorService.getInstanceByID(plan.getInstructorId()));
        course = saveInstance(course);

        if (plan.getCoverImage() != null) {
            String imageUrl = imageStorageService.uploadImage(plan.getCoverImage(), "courses/" + course.getId());
            course.setCoverImageUrl(imageUrl);
            course = super.save(course);
        }
        return course;
    }

    @Override
    public Course saveInstance(Course plan) throws ValidationFailedException {

        if (plan.getCategory() == null) {
            throw new ValidationFailedException("Missing course Type");
        }

        if (StringUtils.isBlank(plan.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(plan.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        Course existingWithTitle = getPlanByTitle(plan.getTitle());

        if (existingWithTitle != null && !existingWithTitle.getId().equals(plan.getId())) {
            throw new ValidationFailedException("A course with the same title already exists!");
        }
        plan.setPublicationStatus(PublicationStatus.INACTIVE);

        return super.merge(plan);

    }

    @Override
    public float getProgress(CourseLecture currentSubTopic) {
        if (currentSubTopic == null) {
            return 0;
        }
        List<CourseLecture> allSubTopics = courseLectureService.getInstances(new Search()
                .addSortAsc("position")
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseTopic.courseLesson.courseLesson", currentSubTopic.getCourseTopic().getCourseLesson().getCourse()), 0, 0);
        if (allSubTopics.isEmpty()) {
            return 0;
        }
        int currentPosition = allSubTopics.indexOf(currentSubTopic) + 1;//the +1 caters for zero based indexing
        return currentPosition * 100 / allSubTopics.size();

    }

    @Override
    public CourseLecture getFirstSubTopic(Course course) throws ValidationFailedException {
        List<CourseLesson> lessons = ApplicationContextProvider.getBean(CourseLessonService.class)
                .getInstances(new Search()
                        .addFilterEqual("course", course)
                        .addSortAsc("position"), 0, 1);
        if (lessons.isEmpty()) {
            throw new ValidationFailedException("No Lessons in Course");
        }

        CourseLesson firstlesson = lessons.get(0);
        List<CourseTopic> topics = ApplicationContextProvider.getBean(CourseTopicService.class)
                .getInstances(new Search()
                        .addFilterEqual("courseLesson", firstlesson)
                        .addSortAsc("position"), 0, 1);
        if (topics.isEmpty()) {
            throw new ValidationFailedException("No topics in first Course lesson");

        }
        CourseTopic firstTopic = topics.get(0);
        List<CourseLecture> subTopics = ApplicationContextProvider.getBean(CourseLectureService.class)
                .getInstances(new Search()
                        .addFilterEqual("courseTopic", firstTopic)
                        .addSortAsc("position"), 0, 1);
        if (subTopics.isEmpty()) {
            throw new ValidationFailedException("No sub Topics in first Course lesson topic");

        }

        return subTopics.get(0);
    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstance(Course plan) {
        plan.setRecordStatus(RecordStatus.DELETED);
        super.save(plan);

    }

    @Override
    public List<Course> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }


    @Override
    public Course getInstanceByID(Long id) {
        return super.findById(id).orElseThrow(() -> new ValidationFailedException(String.format("Course with ID %d not found", id)));
    }

    @Override
    public Course activatePlan(Course plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        Course savedDevotionPlan = super.save(plan);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllStudents(
                    new NotificationBuilder()
                            .setTitle("New Courses added")
                            .setDescription(plan.getTitle())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(plan.getId()))
                            .build());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return savedDevotionPlan;
    }

    @Override
    public Course deActivatePlan(Course plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    @Override
    public Course getPlanByTitle(String planTitle) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("title", planTitle);

        return super.searchUnique(search);

    }

    @Override
    public boolean isDeletable(Course entity) throws OperationFailedException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
