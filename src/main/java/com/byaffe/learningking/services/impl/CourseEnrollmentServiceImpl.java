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
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Transactional
@Repository
public class CourseEnrollmentServiceImpl extends GenericServiceImpl<CourseEnrollment> implements CourseEnrollmentService {


    @Autowired
    CourseLessonService courseLessonService;

    @Autowired
    CourseTopicService courseTopicService;

    @Autowired
    CourseLectureService courseLectureService;

    @Autowired
    CertificateTemplateService certificateTemplateService;

    public static Search generateSearchObjectForEnrollments(String searchTerm) {
        return CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("course.title", "course.description"));
    }

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
    public Double computeRevenue(Search search) {
        return searchUnique(search.addFilterEqual("recordStatus",RecordStatus.ACTIVE).addField("purchasePrice"));
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

    public CourseEnrollment getInstanceByID(Long member_plan_id, boolean includeTemplate) {
        if (member_plan_id == null) {
            return null;
        }
        Search search = new Search().addFilterEqual("id", member_plan_id).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (includeTemplate) {
            search.addFetch("course.certificateTemplate");
        }


        return super.searchUnique(search);
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
        courseEnrollment.setReadStatus(ReadStatus.NotStarted);
        return super.save(courseEnrollment);
    }

    @Override
    public CourseEnrollment startCourse(Long studentId, Long courseId) throws ValidationFailedException {
        Student member = ApplicationContextProvider.getBean(StudentService.class).getStudentById(studentId);
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(courseId);

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

    @Override
    public String generateHtmlCertificate(Long enrolmentId) {
        CourseEnrollment enrollment = getInstanceByID(enrolmentId, true);

        if (enrollment == null) {
            throw new ValidationFailedException("Record not found");
        }

        if (!enrollment.getReadStatus().equals(ReadStatus.Completed)) {
            throw new ValidationFailedException("Certificate not yet available");
        }

        if (enrollment.getCourse() == null) {
            throw new ValidationFailedException("Missing course");
        }

        if (enrollment.getStudent() == null) {
            throw new ValidationFailedException("Missing student");
        }
        if (!enrollment.getCourse().getOffersCertificate()) {
            throw new ValidationFailedException("Course offers no certificates");
        }

        if (enrollment.getCourse().getCertificateTemplate() == null) {
            throw new ValidationFailedException("Course has no certificate template");
        }

        String template = enrollment.getCourse().getCertificateTemplate().getTemplate();
        template = template.replace("{StudentFirstName}", enrollment.getStudent().getUserAccount().getFirstName());
        template = template.replace("{StudentLastName}", enrollment.getStudent().getUserAccount().getLastName());
        template = template.replace("{CourseTitle}", enrollment.getCourse().getTitle());
        template = template.replace("{CompletionDate}", enrollment.getDateCompleted().format(DateTimeFormatter.ISO_DATE));
        template = template.replace("{InstructorName}", enrollment.getCourse().getInstructor().getFullName());

        return template;
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
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(coursePayment.getReferenceRecordId());
        return createActualSubscription(coursePayment.getStudent(), course);
    }

    @Override
    public void createBulkSubscriptions(AggregatorTransaction coursePayment) throws ValidationFailedException {
        if (coursePayment == null || !coursePayment.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
            return;
        }
       StudentService studentService = ApplicationContextProvider.getBean(StudentService.class);
        OrganisationPurchase organisationPurchase=ApplicationContextProvider.getBean(OrganisationPurchaseService.class).getInstanceByID(coursePayment.getReferenceRecordId());
        Course course = ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(organisationPurchase.getRecordId());
        for (Long studentId : organisationPurchase.getEntryIds()) {
            Student student = studentService.getStudentById(studentId);
            createActualSubscription(student, course);
        }
        organisationPurchase.setTransaction(coursePayment);
        organisationPurchase.setRecordStatus(RecordStatus.ACTIVE);
        ApplicationContextProvider.getBean(OrganisationPurchaseService.class).saveInstance(organisationPurchase);


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
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }

    @Override
    public CourseEnrollment completeSubTopic(Student member, CourseLecture lecture) throws ValidationFailedException {
        if (lecture == null || lecture.isNew()) {
            throw new ValidationFailedException("Missing subTopic");
        }
        CourseEnrollment courseEnrollment = getSerieSubscription(member, lecture.getCourseTopic().getCourseLesson().getCourse());

        if (courseEnrollment == null) {
            throw new ValidationFailedException("Not enrolled for this course");
        }

        if (courseEnrollment.getCurrentLecture().getCourseTopic() != lecture.getCourseTopic()) {
            throw new ValidationFailedException("Your previous topic hasnt been completed yet. Please complete all the subtopics in it");
        }

        if (lecture.getPosition() > courseEnrollment.getCurrentLecture().getPosition()) {
            throw new ValidationFailedException("You can't skip previous lectures. Please complete all the lectures in order");

        }

        List<CourseLecture> subTopics = courseLectureService.getInstances(
                new Search()
                        .addSortAsc("position")
                        .addFilterEqual("courseTopic", lecture.getCourseTopic())
                        .addFilterGreaterOrEqual("position", lecture.getPosition()), 0, 1);

        long totalSubTopics = courseLectureService.countInstances(
                new Search().addFilterEqual("courseTopic.courseLesson.course.id", courseEnrollment.getCourseId()));
        long completedSubTopics = courseLectureService.countInstances(
                new Search().addFilterEqual("courseTopic.courseLesson.course.id", courseEnrollment.getCourseId()).addFilterGreaterOrEqual("id", lecture.getId()));
        //Compute Progress
        if (totalSubTopics > 0) {
            double progress = ((double) completedSubTopics / totalSubTopics) * 100;
            courseEnrollment.setProgress(progress);
            if (progress >= 100) {
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
                        .addFilterGreaterOrEqual("position", lecture.getCourseTopic().getPosition() + 1)
                        .addFilterEqual("courseLesson", lecture.getCourseTopic().getCourseLesson())
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
                        .addFilterEqual("position", lecture.getCourseTopic().getCourseLesson().getPosition() + 1)
                        .addFilterEqual("course", lecture.getCourseTopic().getCourseLesson().getCourse())
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
