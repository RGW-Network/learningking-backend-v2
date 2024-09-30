package com.byaffe.learningking.controllers;

import com.byaffe.learningking.controllers.constants.ApiUtils;
import com.byaffe.learningking.dtos.articles.ArticlesFilterDTO;
import com.byaffe.learningking.dtos.courses.*;
import com.byaffe.learningking.dtos.instructor.*;
import com.byaffe.learningking.dtos.courses.CourseResponseDTO;
import com.byaffe.learningking.dtos.courses.CourseTopicResponseDTO;
import com.byaffe.learningking.dtos.courses.LessonResponseDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.impl.CourseServiceImpl;
import com.byaffe.learningking.services.impl.InstructorServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
public class CoursesController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CourseEnrollmentService subscriptionService;

    @GetMapping("")
    public ResponseEntity<ResponseList<CourseResponseDTO>> getCourses(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                      @RequestParam(value = "offset", required = true) Integer offset,
                                                                      @RequestParam(value = "limit", required = true) Integer limit,
                                                                      @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                      @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                      @RequestParam(value = "featured", required = false) Boolean featured,
                                                                      @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                                      @RequestParam(value = "authorId", required = false) Long authorId

    ) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
        if (categoryId != null) {
            search.addFilterEqual("category.id", categoryId);
        }
        if (authorId != null) {
            search.addFilterEqual("instructor.id", authorId);
        }

        if (featured != null) {
            search.addFilterEqual("isFeatured", featured);
        }

        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        long count = ApplicationContextProvider.getBean(CourseService.class).countInstances(search);
        List<CourseResponseDTO> courses = new ArrayList<>();
        for (Course course : ApplicationContextProvider.getBean(CourseService.class).getInstances(search, offset, limit)) {

            CourseResponseDTO dto = modelMapper.map(course, CourseResponseDTO.class);

            int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                    .countInstances(new Search()
                            .addFilterEqual("course", course)
                            .addFilterEqual("recordStatus", RecordStatus.ACTIVE));
            double rattings = 0;

            try {
                rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (UserDetailsContext.getLoggedInStudent() != null) {
                CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), course);

                dto.setEnrolled(subscription != null);
            }
            dto.setNumberOfLessons(lessonsCount);
            dto.setAverageRating((rattings / 5));
            dto.setRatingsCount(ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course));

            courses.add(dto);
        }
        return ResponseEntity.ok().body(new ResponseList<>(courses, count, offset, limit));

    }


    @GetMapping("/by-categories")
    public ResponseEntity<ResponseList<ByTopicResponseDTO>> getCoursesByCategories(@RequestParam(value = "offset", required = true) Integer topicOffset,
                                                                                   @RequestParam(value = "limit", required = true) Integer topicLimit,
                                                                                   @RequestParam(value = "offset", required = true) Integer courseOffset,
                                                                                   @RequestParam(value = "limit", required = true) Integer courseLimit) {
        log.warn("Inside by categories....");
        List<ByTopicResponseDTO> records = new ArrayList<>();
        for (Category devTopic : ApplicationContextProvider.getBean(CategoryService.class).getInstances(new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE), topicOffset, topicLimit)) {
            List<Course> coursesModels = ApplicationContextProvider.getBean(CourseService.class).getInstances(new Search()
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterEqual("publicationStatus", PublicationStatus.ACTIVE)
                    .addFilterEqual("category", devTopic), courseOffset, courseLimit);

            ByTopicResponseDTO courseByTopicResponseDTO = modelMapper.map(devTopic, ByTopicResponseDTO.class);
            List<CourseResponseDTO> dtos = new ArrayList<>();
            for (Course course : coursesModels) {
                CourseResponseDTO dto = modelMapper.map(course, CourseResponseDTO.class);
                int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                        .countInstances(new Search().addFilterEqual("course", course).addFilterEqual("recordStatus", RecordStatus.ACTIVE));
                double rattings = 0;
                try {
                    rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), course);
                dto.setEnrolled(subscription != null);
                dto.setNumberOfLessons(lessonsCount);
                dto.setAverageRating((rattings / 5));
                dto.setRatingsCount(ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course));
                dtos.add(dto);
            }
            courseByTopicResponseDTO.setCourses(dtos);
            records.add(courseByTopicResponseDTO);
        }
        return ResponseEntity.ok().body(new ResponseList<>(records, (int) 0, topicOffset, topicLimit));

    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CourseDetailsResponseDTO>> getCourseDetails(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        CourseService courseService = ApplicationContextProvider.getBean(CourseService.class);
        Course course = courseService.getInstanceByID(id);
        CourseDetailsResponseDTO responseDTO = new CourseDetailsResponseDTO();
        CourseResponseDTO courseObj = modelMapper.map(course, CourseResponseDTO.class);
        List<CourseLesson> lessons = ApplicationContextProvider.getBean(CourseLessonService.class).getInstances(new Search()
                .addFilterEqual("course", course)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addSortAsc("position"), 0, 0);
        CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(member, course);
        for (CourseLesson lesson : lessons) {
            LessonResponseDTO lessonDto = modelMapper.map(lesson, LessonResponseDTO.class);
            lessonDto.setSubscription(subscription);

            lessonDto.setIsPreview(lesson.getPosition() == 1);

            List<CourseTopic> topics = ApplicationContextProvider.getBean(CourseTopicService.class).getInstances(new Search()
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addFilterEqual("courseLesson", lesson)
                    .addSortAsc("position"), 0, 0);

            for (CourseTopic topic : topics) {
                CourseTopicResponseDTO topicJSONObject = modelMapper.map(topic, CourseTopicResponseDTO.class);
                List<CourseLecture> lectures = ApplicationContextProvider.getBean(CourseLectureService.class).getInstances(new Search()
                        .addFilterEqual("courseTopic", topic)
                        .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                        .addSortAsc("position"), 0, 0);
                topicJSONObject.setLectures(lectures.stream().map(r -> modelMapper.map(r, LectureResponseDTO.class)).collect(Collectors.toList()));
                lessonDto.getTopics().add(topicJSONObject);
            }
            courseObj.getLessons().add(lessonDto);
        }

        double rattings = 1;
        try {
            rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course) / 5;
        } catch (Exception e) {
            e.printStackTrace();
        }

        courseObj.setEnrolled(subscription != null);
        courseObj.setAverageRating(rattings / 5);
        if (subscription != null) {
            courseObj.setEnrolled(true);
        }
        log.info("Lessons got: {}", lessons.stream().map(r -> r.id).toArray());
        //     .put("ratingsCount", ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course))
        courseObj.setTestimonials(course.getTestimonials());
        //courseObj.setLessons(lessonsArray);
        courseObj.setNumberOfLessons(lessons.size());
        responseDTO.setCourse(courseObj);
        responseDTO.setSubscription(subscription);
        return ResponseEntity.ok().body(new ResponseObject<>(responseDTO));


    }


    @GetMapping("/lessons/{id}")
    public ResponseEntity<ResponseObject<LessonResponseDTO>> getLessons(@PathVariable("id") Long id) throws JSONException {
        LessonResponseDTO result = new LessonResponseDTO();

        CourseLesson lesson = ApplicationContextProvider.getBean(CourseLessonService.class).getInstanceByID(id);
        if (lesson == null) {
            throw new ValidationFailedException("Lesson not found");
        }
        result = modelMapper.map(lesson, LessonResponseDTO.class);
        CourseTopicService courseSubTopicService = ApplicationContextProvider.getBean(CourseTopicService.class);
        List<CourseTopic> topics = courseSubTopicService.getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseLesson", lesson)
                .addSortAsc("position"), 0, 0);
        CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), lesson.getCourse());

        for (CourseTopic topic : topics) {
            CourseTopicResponseDTO topicJSONObject = modelMapper.map(topic, CourseTopicResponseDTO.class);
            if (subscription != null) {
                topicJSONObject.setSubscription(subscription);
            }
            List<CourseLecture> lectures = ApplicationContextProvider.getBean(CourseLectureService.class).getInstances(new Search()
                    .addFilterEqual("courseTopic", topic)
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                    .addSortAsc("position"), 0, 0);
            topicJSONObject.setLectures(lectures.stream().map(r -> modelMapper.map(r, LectureResponseDTO.class)).collect(Collectors.toList()));


            result.getTopics().add(topicJSONObject);
        }
        result.setSubscription(subscription);
        result.setIsPreview(lesson.getPosition() == 1);
        return ResponseEntity.ok().body(new ResponseObject<>(result));
    }


    @GetMapping("/topic/{id}")
    public ResponseEntity<ResponseObject<CourseTopicResponseDTO>> getTopicById(@PathVariable("id") Long id) throws JSONException {
        CourseTopicResponseDTO result = new CourseTopicResponseDTO();
        Student member = UserDetailsContext.getLoggedInStudent();

        CourseTopic topic = ApplicationContextProvider.getBean(CourseTopicService.class).getInstanceByID(id);
        if (topic == null) {
            throw new ValidationFailedException("Topic not found");
        }
        result = modelMapper.map(topic, CourseTopicResponseDTO.class);
        List<CourseLecture> subTopics = ApplicationContextProvider.getBean(CourseLectureService.class
        ).getInstances(new Search()
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE)
                .addFilterEqual("courseTopic", topic)
                .addSortAsc("position"), 0, 0);
        CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(member, topic.getCourseLesson().getCourse());

        for (CourseLecture subTopic : subTopics) {
            LectureResponseDTO jSONObject = modelMapper.map(subTopic, LectureResponseDTO.class);
            result.getLectures().add(jSONObject);
        }
        result.setSubscription(subscription);
        return ResponseEntity.ok().body(new ResponseObject<>(result));

    }


    @PostMapping("/enroll/{id}")
    public ResponseEntity<ResponseObject<CourseEnrollment>> enroll(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        assert member != null;
        CourseEnrollment courseEnrollment = subscriptionService.enrolForFreeCourse(member.getId(), id);
        return ResponseEntity.ok().body(new ResponseObject<>(courseEnrollment));

    }

    @PostMapping("/start/{id}")
    public ResponseEntity<ResponseObject<CourseEnrollment>> start(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        assert member != null;
        CourseEnrollment courseEnrollment = subscriptionService.enrolForFreeCourse(member.getId(), id);
        return ResponseEntity.ok().body(new ResponseObject<>(courseEnrollment));
    }


    @PostMapping("/rating")
    public ResponseEntity<ResponseObject<CourseRating>> rateCourse(@RequestBody CourseRatingDTO courseRatingDTO) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        if (member == null) {
            throw new ValidationFailedException("Student Not Found");
        }
        return ResponseEntity.ok().body(new ResponseObject<>(ApplicationContextProvider.getBean(CourseRatingService.class).saveInstance(courseRatingDTO)));
    }


    @GetMapping("/ratings")
    public ResponseEntity<ResponseList<CourseRatingResponseDTO>> getRatings(@RequestParam(value = "courseId", required = false) Long courseId,
                                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                                            @RequestParam(value = "limit", required = true) Integer limit) throws JSONException {
        Search search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);

        if (courseId != null) {
            search.addFilterEqual("course.id", courseId);
        }

        List<CourseRating> courseRatings = ApplicationContextProvider.getBean(CourseRatingService.class).getInstances(search, offset, limit);

        List<CourseRatingResponseDTO> ratings = new ArrayList<>();
        for (CourseRating courseRating : courseRatings) {
            CourseRatingResponseDTO dto = new CourseRatingResponseDTO();
            dto.setStars(courseRating.getStarsCount());
            dto.setDateCreated(ApiUtils.ENGLISH_DATE_FORMAT.format(courseRating.getDateCreated()));
            dto.setStudentFullName(courseRating.getStudent().getFullName());
            dto.setRatingText(courseRating.getReviewText());
            ratings.add(dto);

        }
        return ResponseEntity.ok().body(new ResponseList<>(ratings, 0, offset, limit));

    }


    @PostMapping("/lectures/complete/{id}")
    public ResponseEntity<CourseEnrollment> completeSubTopic(@PathVariable("id") Long id) throws JSONException {
        Student member = UserDetailsContext.getLoggedInStudent();
        if (member == null) {
            throw new ValidationFailedException("Student Not Found");
        }
        CourseLecture topic = ApplicationContextProvider.getBean(CourseLectureService.class).getInstanceByID(id);
        if (topic == null) {
            throw new ValidationFailedException("Topic  Not Found");
        }
        CourseEnrollment courseEnrollment = ApplicationContextProvider.getBean(CourseEnrollmentService.class).completeSubTopic(member, topic);

        return ResponseEntity.ok().body(courseEnrollment);
    }


    @GetMapping("/mycourses")
    public ResponseEntity<ResponseList<CourseResponseDTO>> getStudentCourses(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                             @RequestParam(value = "offset", required = true) Integer offset,
                                                                             @RequestParam(value = "limit", required = true) Integer limit,
                                                                             @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                             @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                             @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = CourseServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        long totalRecords = ApplicationContextProvider.getBean(CourseEnrollmentService.class).countInstances(search);
        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        List<CourseResponseDTO> courses = new ArrayList<>();
        for (CourseEnrollment course : ApplicationContextProvider.getBean(CourseEnrollmentService.class).getInstances(search, offset, limit)) {
            CourseResponseDTO dto = modelMapper.map(course.getCourse(), CourseResponseDTO.class);
            int lessonsCount = ApplicationContextProvider.getBean(CourseLessonService.class)
                    .countInstances(new Search()
                            .addFilterEqual("course", course.getCourse())
                            .addFilterEqual("recordStatus", RecordStatus.ACTIVE));
            double rattings = 0;
            try {
                rattings = ApplicationContextProvider.getBean(CourseRatingService.class).getTotalCourseRatings(course.getCourse()) / 5;
            } catch (Exception e) {
                e.printStackTrace();
            }
            CourseEnrollment subscription = ApplicationContextProvider.getBean(CourseEnrollmentService.class).getSerieSubscription(UserDetailsContext.getLoggedInStudent(), course.getCourse());
            dto.setEnrolled(subscription != null);
            dto.setNumberOfLessons(lessonsCount);
            dto.setSubscription(subscription);
            dto.setAverageRating((rattings / 5));
            dto.setRatingsCount(ApplicationContextProvider.getBean(CourseRatingService.class).getRatingsCount(course.getCourse()));
            courses.add(dto);
        }
        return ResponseEntity.ok().body(new ResponseList<>(courses, totalRecords, offset, limit));


    }

    @GetMapping("/instructors")
    public ResponseEntity<ResponseList<InstructorResponseDTO>> getInstructors(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "offset", required = true) Integer offset,
            @RequestParam(value = "limit", required = true) Integer limit
    ) {
        Search search = InstructorServiceImpl.generateSearchObjectForCourses(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        long count = ApplicationContextProvider.getBean(InstructorService.class).countInstances(search);
        log.info("Instructors Count: {}", count);
        List<CourseInstructor> courses = ApplicationContextProvider.getBean(InstructorService.class).getInstances(search, offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(courses.stream().map(r -> modelMapper.map(r, InstructorResponseDTO.class)).collect(Collectors.toList()), count, offset, limit));
    }


}
