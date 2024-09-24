package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.ReadStatus;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.models.payments.StudentSubscriptionPlan;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Repository
public class CourseEnrollmentServiceImpl extends BaseDAOImpl<CourseEnrollment> implements CourseEnrollmentService {


    @Autowired
    CourseLessonService courseLessonService;

    @Autowired
    CourseTopicService courseTopicService;

    @Autowired
    CourseLectureService courseLectureService;

    @Override
    public CourseEnrollment saveInstance(CourseEnrollment subscription) throws ValidationFailedException {
        CourseEnrollment exists = getSerieSubscription(subscription.getStudent(), subscription.getCourse());

        if (exists != null && !exists.getId().equals(subscription.getId())) {
            subscription.setId(exists.getId());
        }

        return super.merge(subscription);
    }

    @Override
    public CourseEnrollment createSubscription(Course course, Student member) {
        CourseEnrollment exists = getSerieSubscription(member, course);

        if (exists == null) {

            CourseEnrollment subscription = new CourseEnrollment();
            subscription.setCourse(course);
            subscription.setStudent(member);
            return super.merge(subscription);
        }
        return exists;
    }

    @Override
    public List<CourseEnrollment> getPlansForStudent(Student member) {
        return super.searchByPropertyEqual("student", member, RecordStatus.ACTIVE); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CourseEnrollment> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        return super.search(search.setFirstResult(offset).setMaxResults(limit)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteInstance(CourseEnrollment memberPlan) {
        memberPlan.setRecordStatus(RecordStatus.DELETED);
        super.save(memberPlan);//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CourseEnrollment getInstanceByID(Long member_plan_id) {
        if (member_plan_id == null) {
            return null;
        }
        return super.searchUniqueByPropertyEqual("id", member_plan_id, RecordStatus.ACTIVE);
    }

    @Override
    public CourseEnrollment createSubscription(Student member, Course course) throws ValidationFailedException {

        if (course.getIsPaid()) {
            throw new ValidationFailedException("This is a paid Course");
        }
        return createActualSubscription(member, course);
    }

    @Override
    public CourseEnrollment enrolForFreeCourse(Long studentId, Long courseId) throws ValidationFailedException {
        Student member = ApplicationContextProvider.getBean(StudentService.class).getStudentById(studentId);
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(courseId);
        if (course.getIsPaid()) {
            throw new ValidationFailedException("This is a paid Course");
        }

        CourseEnrollment existing = getSerieSubscription(member, course);
        if (existing != null) {
            throw new ValidationFailedException("You already enrolled for this course");
        }
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        CourseLecture firstSubTopic = null;
        firstSubTopic = ApplicationContextProvider.getBean(CourseService.class).getFirstSubTopic(course);
        courseEnrollment.setStudent(member);
        courseEnrollment.setCourse(course);
        courseEnrollment.setProgress(0.0);
        courseEnrollment.setCurrentLecture(firstSubTopic);
        courseEnrollment.setReadStatus(ReadStatus.Inprogress);
        return super.save(courseEnrollment);
    }

    @Override
    public CourseEnrollment startCourse(Long studentId, Long courseId) throws ValidationFailedException {
        Student member = ApplicationContextProvider.getBean(StudentService.class).getStudentById(studentId);
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(courseId);
        if (course.getIsPaid()) {
            throw new ValidationFailedException("This is a paid Course");
        }

        CourseEnrollment courseEnrollment = getSerieSubscription(member, course);
        if (courseEnrollment == null) {
            throw new ValidationFailedException("You have not enrolled for this course");
        }
        if (courseEnrollment.getReadStatus() != ReadStatus.NotStarted) {
            throw new ValidationFailedException("You already started this course");
        }
        courseEnrollment.setReadStatus(ReadStatus.Inprogress);
        return super.save(courseEnrollment);
    }

    private CourseEnrollment createActualSubscription(Student member, Course course) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        CourseLecture firstSubTopic = null;
        try {
            firstSubTopic = ApplicationContextProvider.getBean(CourseService.class).getFirstSubTopic(course);
        } catch (ValidationFailedException ex) {
            ex.printStackTrace();
            courseEnrollment.setLastErrorMessage(ex.getMessage());
        } finally {
            courseEnrollment.setStudent(member);
            courseEnrollment.setCourse(course);
            courseEnrollment.setCurrentLecture(firstSubTopic);
            courseEnrollment.setReadStatus(ReadStatus.Inprogress);
            return super.save(courseEnrollment);
        }
    }

    @Override
    public CourseEnrollment createActualSubscription(Course course, StudentSubscriptionPlan memberSubscriptionPlan) throws ValidationFailedException {
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        CourseLecture firstSubTopic = null;
        try {
            firstSubTopic = ApplicationContextProvider.getBean(CourseService.class).getFirstSubTopic(course);
            courseEnrollment.setStudent(memberSubscriptionPlan.getStudent());
            courseEnrollment.setStudentSubscriptionPlan(memberSubscriptionPlan);
            courseEnrollment.setCourse(course);
            courseEnrollment.setCurrentLecture(firstSubTopic);
            courseEnrollment.setReadStatus(ReadStatus.Inprogress);
        } catch (ValidationFailedException ex) {
            courseEnrollment.setLastErrorMessage(ex.getMessage());
        }
        return super.save(courseEnrollment);

    }

    @Override
    public CourseEnrollment createSubscription(AggregatorTransaction coursePayment) throws ValidationFailedException {
        if (coursePayment == null || !coursePayment.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
            return null;
        }
        Course course=ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(coursePayment.getReferenceRecordId());
        return createActualSubscription(coursePayment.getStudent(), course);
    }

    @Override
    public CourseEnrollment getSerieSubscription(Student member, Course course) {
        if (member == null || course == null) {
            return null;
        }

        Search search = new Search().addFilterEqual("student", member)
                .addFilterEqual("course", course).setMaxResults(1);

        return super.searchUnique(search);


    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstances(Search search) throws OperationFailedException {
        // super.delete(entity);
    }

    @Override
    public CourseEnrollment completeSubTopic(Student member, CourseLecture subTopic) throws ValidationFailedException {
        if (subTopic == null || subTopic.isNew()) {
            throw new ValidationFailedException("Missing subTopic");
        }
        CourseEnrollment courseEnrollment = getSerieSubscription(member, subTopic.getCourseTopic().getCourseLesson().getCourse());

        if (courseEnrollment == null) {
            throw new ValidationFailedException("Not enrolled for this course");
        }

        if (courseEnrollment.getCurrentLecture().getCourseTopic() != subTopic.getCourseTopic()) {

            throw new ValidationFailedException("Your previous topic hasnt been completed yet. Please complete all the subtopics in it");

        }

        if (subTopic.getPosition() > courseEnrollment.getCurrentLecture().getPosition()) {
            throw new ValidationFailedException("You can't skip previous subtopics. Please complete all the subtopics in order");

        }

        List<CourseLecture> subTopics = courseLectureService.getInstances(
                new Search()
                        .addSortAsc("position")
                        .addFilterEqual("courseTopic", subTopic.getCourseTopic())
                        .addFilterGreaterOrEqual("position", subTopic.getPosition()), 0, 1);

        long totalSubTopics= courseLectureService.countInstances(
                new Search().addFilterEqual("courseTopic.courseLesson.course.id", courseEnrollment.getCourseId()));
        long completedSubTopics= courseLectureService.countInstances(
                new Search().addFilterEqual("courseTopic.courseLesson.course.id", courseEnrollment.getCourseId()).addFilterGreaterOrEqual("id",subTopic.getId()));
       //Compute Progress
        if(totalSubTopics>0) {
           double progress = ((double) completedSubTopics / totalSubTopics) * 100;
           courseEnrollment.setProgress(progress);
           if(progress>=100){
               courseEnrollment.setReadStatus(ReadStatus.Completed);
               courseEnrollment.setDateCompleted(LocalDateTime.now());

               return save(courseEnrollment);
           }
       }
        //If subtopic of higher position exists in topic
        if (!subTopics.isEmpty()) {
            courseEnrollment.setCurrentLecture(subTopics.get(0));
            courseEnrollment = saveInstance(courseEnrollment);
            return courseEnrollment;
        }

        //look for next topic
        List<CourseTopic> topics = courseTopicService.getInstances(
                new Search()
                        .addFilterGreaterOrEqual("position", subTopic.getCourseTopic().getPosition() + 1)
                        .addFilterEqual("courseLesson", subTopic.getCourseTopic().getCourseLesson())
                        .addSortAsc("position"), 0, 1);
        CourseTopic nextTopic = topics.get(0);

        //Next topic exists on lesson
        if (nextTopic != null) {
            //get first subtopic for this next topic

            CourseLecture nextSubTopic = courseLectureService.getFirstSubTopic(nextTopic);
            courseEnrollment.setCurrentLecture(nextSubTopic);
            courseEnrollment = saveInstance(courseEnrollment);
            return courseEnrollment;

        }
        // Fetch next lesson
        List<CourseLesson> lessons = courseLessonService.getInstances(
                new Search()
                        .addFilterEqual("position", subTopic.getCourseTopic().getCourseLesson().getPosition() + 1)
                        .addFilterEqual("course", subTopic.getCourseTopic().getCourseLesson().getCourse())
                        .addSortAsc("position"), 0, 1);
        CourseLesson nextLesson = lessons.get(0);

        //Next lessson exists
        if (nextLesson != null) {
            //Fetch first sub-topic in next lesson
            CourseLecture nextSubTopicInLesson = courseLectureService.getFirstSubTopic(nextLesson);
            courseEnrollment.setCurrentLecture(nextSubTopicInLesson);
            courseEnrollment = saveInstance(courseEnrollment);
            return courseEnrollment;

        }
        //Next lesson doesnt exist. So lets just complete the course
        courseEnrollment.setProgress(100.0);
        courseEnrollment.setReadStatus(ReadStatus.Completed);
        courseEnrollment = saveInstance(courseEnrollment);

        //Update certification progress too
        ApplicationContextProvider.getBean(CertificationSubscriptionService.class).completeCertificationCourse(member, courseEnrollment.getCourse());
        return courseEnrollment;

    }


}
